package com.mercari.naventryscope.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSType
import com.mercari.naventryscope.processor.NavEntryScopeNames.NAV_ENTRY_COMPONENT_SHORT_NAME
import com.mercari.naventryscope.processor.NavEntryScopeNames.NAV_ENTRY_COMPONENT_STORE_SHORT_NAME
import com.mercari.naventryscope.processor.NavEntryScopeNames.NAV_ENTRY_ENTRY_POINT_SHORT_NAME
import com.mercari.naventryscope.processor.NavEntryScopeNames.NAV_ENTRY_LIB_PACKAGE_NAME
import com.mercari.naventryscope.processor.NavEntryScopeNames.NAV_ENTRY_MODULE_SHORT_NAME
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

@Suppress("SpreadOperator")
internal class NavEntryScopeCodeGenerator(
    private val codeGenerator: CodeGenerator,
    private val rootPackageName: String,
    private val sourceDependencies: Array<KSFile>,
) {

    fun generateEntryPoint(types: List<KSType>) {
        val interfaceBuilder = TypeSpec.interfaceBuilder(NAV_ENTRY_ENTRY_POINT_SHORT_NAME)
            .addModifiers(KModifier.INTERNAL)
            .addAnnotation(EntryPointAnnotations.entryPoint)
            .addAnnotation(EntryPointAnnotations.installInNavEntryComponent)

        types.forEach { type ->
            val funSpec = FunSpec
                .builder(type.toEntryPointMethodName())
                .addModifiers(KModifier.ABSTRACT)
                .returns(type.toClassName())
                .build()

            interfaceBuilder.addFunction(funSpec)
        }

        val fileSpec = FileSpec.builder(rootPackageName, NAV_ENTRY_ENTRY_POINT_SHORT_NAME)
            .addType(interfaceBuilder.build())
            .build()

        fileSpec.writeTo(
            codeGenerator = codeGenerator,
            dependencies = Dependencies(
                aggregating = true,
                *sourceDependencies,
            )
        )
    }

    fun generateModule(types: List<KSType>) {
        val entryPointInterfaceName = ClassName(rootPackageName, NAV_ENTRY_ENTRY_POINT_SHORT_NAME)

        val classBuilder = TypeSpec.objectBuilder(NAV_ENTRY_MODULE_SHORT_NAME)
            .addModifiers(KModifier.INTERNAL)
            .addAnnotation(ModuleAnnotations.module)
            .addAnnotation(ModuleAnnotations.installInViewModelComponent)

        for (type in types) {
            val moduleMethodName = type.toModuleMethodName()
            val entryPointMethodName = type.toEntryPointMethodName()

            val navEntryComponentStoreParam = ParameterSpec.builder(
                "navEntryComponentStore",
                ClassName(NAV_ENTRY_LIB_PACKAGE_NAME, NAV_ENTRY_COMPONENT_STORE_SHORT_NAME)
            ).build()

            val savedStateHandleParam = ParameterSpec.builder(
                "savedStateHandle",
                ClassName(LIFECYCLE_PACKAGE_NAME, "SavedStateHandle")
            ).build()

            val funSpec = FunSpec.builder(moduleMethodName)
                .addAnnotation(ModuleAnnotations.provides)
                .addParameter(navEntryComponentStoreParam)
                .addParameter(savedStateHandleParam)
                .returns(type.toClassName())
                .addStatement(
                    "val navEntryScopeId = savedStateHandle.get<String>(%T.NAV_ENTRY_SCOPE_ID).orEmpty()",
                    ClassName(NAV_ENTRY_LIB_PACKAGE_NAME, NAV_ENTRY_COMPONENT_SHORT_NAME)
                )
                .addStatement("val component = navEntryComponentStore.getComponent(navEntryScopeId)")
                .addStatement(
                    "return %T.get(component, %T::class.java).%L()",
                    ClassName(HILT_PACKAGE_NAME, "EntryPoints"),
                    entryPointInterfaceName,
                    entryPointMethodName,
                )
                .build()

            classBuilder.addFunction(funSpec)
        }

        val fileSpec = FileSpec.builder(rootPackageName, NAV_ENTRY_MODULE_SHORT_NAME)
            .addType(classBuilder.build())
            .build()

        fileSpec.writeTo(
            codeGenerator = codeGenerator,
            dependencies = Dependencies(
                aggregating = true,
                *sourceDependencies,
            )
        )
    }

    private fun KSType.toEntryPointMethodName() = NavEntryScopeNames.entryPointMethodName(
        rootPackageName = rootPackageName,
        packageName = declaration.packageName.asString(),
        typeName = declaration.simpleName.asString(),
    )

    private fun KSType.toModuleMethodName() = NavEntryScopeNames.moduleMethodName(
        rootPackageName = rootPackageName,
        packageName = declaration.packageName.asString(),
        typeName = declaration.simpleName.asString(),
    )

    private companion object {
        const val HILT_PACKAGE_NAME = "dagger.hilt"
        const val LIFECYCLE_PACKAGE_NAME = "androidx.lifecycle"
    }

    private object EntryPointAnnotations {
        val entryPoint = AnnotationSpec
            .builder(ClassName(HILT_PACKAGE_NAME, "EntryPoint"))
            .build()

        val installInNavEntryComponent = AnnotationSpec
            .builder(ClassName(HILT_PACKAGE_NAME, "InstallIn"))
            .addMember("%T::class", ClassName(NAV_ENTRY_LIB_PACKAGE_NAME, NAV_ENTRY_COMPONENT_SHORT_NAME))
            .build()
    }

    private object ModuleAnnotations {
        val module = AnnotationSpec
            .builder(ClassName("dagger", "Module"))
            .build()

        val installInViewModelComponent = AnnotationSpec
            .builder(ClassName(HILT_PACKAGE_NAME, "InstallIn"))
            .addMember("%T::class", ClassName("dagger.hilt.android.components", "ViewModelComponent"))
            .build()

        val provides = AnnotationSpec
            .builder(ClassName("dagger", "Provides"))
            .build()
    }
}
