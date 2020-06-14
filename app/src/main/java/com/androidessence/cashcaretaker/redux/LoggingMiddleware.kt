package com.androidessence.cashcaretaker.redux

import com.androidessence.cashcaretaker.logging.Logger

class LoggingMiddleware(private val logger: Logger) : Middleware {
    override fun dispatch(action: Action, next: NextDispatcher) {
        logger.debug("Dispatching action: $action:")
        next.dispatch(action)
    }
}
