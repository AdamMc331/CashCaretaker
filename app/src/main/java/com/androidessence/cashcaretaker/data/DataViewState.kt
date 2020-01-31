package com.androidessence.cashcaretaker.data

sealed class DataViewState {
    object Loading : DataViewState()
    object Empty : DataViewState()
    class Success<T>(val result: List<T>) : DataViewState()
    class Error(val error: Throwable?) : DataViewState()
}
