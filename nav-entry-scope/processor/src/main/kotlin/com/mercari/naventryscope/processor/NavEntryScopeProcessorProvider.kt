package com.mercari.naventryscope.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class NavEntryScopeProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = NavEntryScopeProcessor(
        codeGenerator = environment.codeGenerator,
        kspLogger = environment.logger,
        options = environment.options
    )
}
