package com.androidessence.cashcaretaker.redux

interface Middleware {
    fun dispatch(action: Action, next: NextDispatcher)
}
