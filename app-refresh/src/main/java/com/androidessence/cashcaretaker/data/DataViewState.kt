package com.androidessence.cashcaretaker.data

sealed class DataViewState {
    class Loading : DataViewState()
    class Empty : DataViewState()
    class Success<T>(val result: List<T>) : DataViewState()
    class Error(val error: Throwable?) : DataViewState()
}