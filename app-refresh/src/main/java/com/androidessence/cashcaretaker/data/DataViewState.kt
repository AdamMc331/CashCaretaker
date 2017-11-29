package com.androidessence.cashcaretaker.data

sealed class DataViewState {
    class Initialized : DataViewState()
    class Loading : DataViewState()
    class ListSuccess<out T>(val items: List<T>) : DataViewState()
    class Error(val error: Throwable?) : DataViewState()
}