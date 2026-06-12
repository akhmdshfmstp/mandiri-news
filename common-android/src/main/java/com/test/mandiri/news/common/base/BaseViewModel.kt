package com.test.mandiri.news.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

abstract class BaseViewModel : ViewModel() {

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    open fun handleError(throwable: Throwable) {
        // Override in subclasses to handle errors
    }
}
