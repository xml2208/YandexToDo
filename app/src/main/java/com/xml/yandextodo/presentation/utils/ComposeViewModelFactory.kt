package com.xml.yandextodo.presentation.utils

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import javax.inject.Inject
import javax.inject.Provider

//class ComposeViewModelFactory @Inject constructor(
//    private val viewModelMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        val creator = viewModelMap[modelClass]
//            ?: viewModelMap.entries.find { modelClass.isAssignableFrom(it.key) }?.value
//            ?: throw IllegalArgumentException("Unknown model class: $modelClass")
//
//        @Suppress("UNCHECKED_CAST")
//        return creator.get() as T
//    }
//}
//
//@Composable
//inline fun <reified VM : ViewModel> daggerViewModel(
//    factory: ComposeViewModelFactory
//): VM {
//    return viewModel(
//        modelClass = VM::class.java,
//        factory = factory
//    )
//}