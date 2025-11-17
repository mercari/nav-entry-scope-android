package com.mercari.naventryscope

import dagger.hilt.DefineComponent

@DefineComponent.Builder
interface NavEntryComponentBuilder {
    fun build(): NavEntryComponent
}
