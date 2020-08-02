package com.adammcneilly.cashcaretaker.analytics

import org.koin.dsl.module

val analyticsModule = module {
    single<AnalyticsTracker> {
        FirebaseAnalyticsTracker()
    }
}
