package com.mercari.naventryscope.processor

import kotlin.test.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NavEntryScopeNamesTest {

    private val rootPackageName = "com.mercari.test"
    private val typeName = "NavEntryNames"

    @Nested
    inner class EntryPointMethodNameTests {
        @Test
        fun `entryPointMethodName returns the camelCased class name when root and current package names are the same`() {
            val actual = NavEntryScopeNames.entryPointMethodName(
                rootPackageName = rootPackageName,
                packageName = rootPackageName,
                typeName = typeName,
            )

            assertEquals("navEntryNames", actual)
        }

        @Test
        fun `entryPointMethodName returns the camelCased package name and PamelCased type name when package is a subpackage of root`() {
            val actual = NavEntryScopeNames.entryPointMethodName(
                rootPackageName = rootPackageName,
                packageName = "$rootPackageName.sub.package",
                typeName = typeName,
            )

            assertEquals("subPackage_NavEntryNames", actual)
        }

        @Test
        fun `entryPointMethodName returns the camelCased package name and PamelCased type name when package is not a subpackage of root`() {
            val actual = NavEntryScopeNames.entryPointMethodName(
                rootPackageName = rootPackageName,
                packageName = "com.mercari.other.sub.package",
                typeName = typeName,
            )

            assertEquals("comMercariOtherSubPackage_NavEntryNames", actual)
        }
    }

    @Nested
    inner class ModuleMethodNameTests {
        @Test
        fun `moduleMethodName returns providePascalCased class name when  root and current package names are the same`() {
            val actual = NavEntryScopeNames.moduleMethodName(
                rootPackageName = rootPackageName,
                packageName = rootPackageName,
                typeName = typeName,
            )

            assertEquals("provideNavEntryNames", actual)
        }

        @Test
        fun `moduleMethodName returns PamelCased subpackage name and PamelCased type name when package is a subpackage of root`() {
            val actual = NavEntryScopeNames.moduleMethodName(
                rootPackageName = rootPackageName,
                packageName = "$rootPackageName.sub.package",
                typeName = typeName,
            )

            assertEquals("provide_SubPackage_NavEntryNames", actual)
        }

        @Test
        fun `moduleMethodName returns the PamelCased package name and PamelCased type name when package is not a subpackage of root`() {
            val actual = NavEntryScopeNames.moduleMethodName(
                rootPackageName = rootPackageName,
                packageName = "com.mercari.other.sub.package",
                typeName = typeName,
            )

            assertEquals("provide_ComMercariOtherSubPackage_NavEntryNames", actual)
        }
    }
}
