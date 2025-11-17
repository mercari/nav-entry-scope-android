package com.mercari.naventryscope.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.FunctionKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.mercari.naventryscope.processor.NavEntryScopeNames.NAV_ENTRY_SCOPED_QUALIFIED_NAME
import com.mercari.naventryscope.processor.NavEntryScopeNames.NAV_ENTRY_SCOPED_SHORT_NAME

class NavEntryScopeProcessor(
    private val codeGenerator: CodeGenerator,
    private val kspLogger: KSPLogger,
    options: Map<String, String>,
) : SymbolProcessor {

    private val rootPackageName: String by lazy {
        options.getOrDefault(COMPILER_OPTION_PACKAGE_KEY, DEFAULT_PACKAGE)
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val dependingFiles = mutableListOf<KSFile?>()
        val types = resolver
            .getSymbolsWithAnnotation(NAV_ENTRY_SCOPED_QUALIFIED_NAME)
            .mapNotNull { annotated ->
                when {
                    annotated is KSClassDeclaration -> {
                        if (annotated.typeParameters.isNotEmpty()) {
                            kspLogger.warn(
                                "Unsupported @$NAV_ENTRY_SCOPED_SHORT_NAME typed class: $annotated. Skipped.",
                                annotated,
                            )
                            null
                        } else {
                            dependingFiles.add(annotated.containingFile)
                            annotated.asType(emptyList())
                        }
                    }

                    annotated is KSFunctionDeclaration && annotated.functionKind == FunctionKind.MEMBER -> {
                        dependingFiles.add(annotated.containingFile)
                        annotated.returnType?.resolve()
                    }

                    else -> {
                        kspLogger.warn(
                            "Unknown @$NAV_ENTRY_SCOPED_SHORT_NAME element: $annotated. Skipped.",
                            annotated,
                        )
                        null
                    }
                }
            }.toList()

        if (types.isNotEmpty()) {
            NavEntryScopeCodeGenerator(
                codeGenerator = codeGenerator,
                rootPackageName = rootPackageName,
                sourceDependencies = dependingFiles.filterNotNull().distinct().toTypedArray(),
            ).run {
                generateEntryPoint(types)
                generateModule(types)
            }
        }

        return emptyList()
    }

    private companion object {
        const val COMPILER_OPTION_PACKAGE_KEY = "com.mercari.naventryscope.package"
        const val DEFAULT_PACKAGE = "com.mercari.naventryscope.generated"
    }
}
