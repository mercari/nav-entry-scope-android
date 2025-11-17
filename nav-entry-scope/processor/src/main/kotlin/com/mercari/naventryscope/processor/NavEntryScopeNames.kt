package com.mercari.naventryscope.processor

internal object NavEntryScopeNames {

    fun entryPointMethodName(
        rootPackageName: String,
        packageName: String,
        typeName: String,
    ): String {
        val subPackageName = packageName.removePrefix("$rootPackageName.")

        return if (rootPackageName == packageName || subPackageName.isEmpty()) {
            typeName.replaceFirstChar { it.lowercase() }.ifEmpty { "anonymous" }
        } else {
            val packagePart = subPackageName.split('.')
                .joinToString("") { segment ->
                    segment.replaceFirstChar { it.uppercase() }
                }.replaceFirstChar { it.lowercase() }

            val classNamePart = typeName.ifEmpty { "Anonymous" }

            return "${packagePart}_$classNamePart"
        }
    }

    fun moduleMethodName(
        rootPackageName: String,
        packageName: String,
        typeName: String,
    ): String {
        val classNamePart = typeName.ifEmpty { "Anonymous" }

        if (packageName == rootPackageName) {
            return "provide$classNamePart"
        }

        // remove the root package prefix for a shorter method name
        val subPackage = packageName.removePrefix("$rootPackageName.")

        // convert remaining package segments to pascal case
        val packagePart = subPackage.split('.')
            .joinToString("") { segment ->
                segment.replaceFirstChar { it.uppercase() }
            }

        return "provide_${packagePart}_$classNamePart"
    }

    const val NAV_ENTRY_LIB_PACKAGE_NAME = "com.mercari.naventryscope"

    const val NAV_ENTRY_SCOPED_SHORT_NAME = "NavEntryScoped"
    const val NAV_ENTRY_SCOPED_QUALIFIED_NAME = "$NAV_ENTRY_LIB_PACKAGE_NAME.$NAV_ENTRY_SCOPED_SHORT_NAME"

    const val NAV_ENTRY_COMPONENT_SHORT_NAME = "NavEntryComponent"
    const val NAV_ENTRY_COMPONENT_STORE_SHORT_NAME = "NavEntryComponentStore"

    const val NAV_ENTRY_ENTRY_POINT_SHORT_NAME = "NavEntryEntryPoint"
    const val NAV_ENTRY_MODULE_SHORT_NAME = "NavEntryModule"
}
