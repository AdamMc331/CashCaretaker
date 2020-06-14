package com.androidessence.cashcaretaker.redux

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class StoreTest {
    @Test
    fun callReducerWithoutMiddleware() {
        val initialState = TestState()
        val reducer = TestReducer()

        val store = Store(
            initialState = initialState,
            reducer = reducer
        )

        store.dispatch(TestAction())

        reducer.assertReduceCalls(1)
    }

    @Test
    fun callMiddlewareBeforeReducer() {
        val callList: MutableList<String> = mutableListOf()

        val initialState = TestState()
        val reducer = object : Reducer<State> {
            override fun reduce(state: State, action: Action): State {
                callList.add("Reducer")
                return state
            }
        }
        val middleware = object : Middleware {
            override fun dispatch(action: Action, next: NextDispatcher) {
                callList.add("Middleware")
                next.dispatch(action)
            }
        }

        val store = Store(
            initialState = initialState,
            reducer = reducer,
            middlewares = listOf(middleware)
        )

        store.dispatch(TestAction())

        val expectedCalls = listOf("Middleware", "Reducer")
        assertEquals(expectedCalls, callList)
    }
}

private class TestAction : Action

private class TestState : State

private class TestReducer : Reducer<State> {
    private var reduceCallCount = 0

    override fun reduce(state: State, action: Action): State {
        reduceCallCount++
        return state
    }

    fun assertReduceCalls(expectedCount: Int) {
        assertEquals(expectedCount, reduceCallCount)
    }
}
