package com.androidessence.cashcaretaker.redux

interface Reducer<S : State> {
    fun reduce(state: S, action: Action): S
}
