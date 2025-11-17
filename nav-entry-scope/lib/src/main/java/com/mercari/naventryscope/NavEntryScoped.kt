package com.mercari.naventryscope

import javax.inject.Scope

/**
 * Scope annotation for bindings that should exist for the life of a Navigation Back Stack Entry.
 *
 * The scope lies between the Activity and ViewModel scopes, and it allows to share dependencies across different
 * ViewModels on the same screen.
 *
 * To get access to `NavEntryScoped` dependencies from ViewModels, inject the ViewModel through the
 * [navEntryScopedViewModel] function.
 *
 * @see NavEntryComponent
 */
@Scope
@Retention(AnnotationRetention.BINARY)
annotation class NavEntryScoped
