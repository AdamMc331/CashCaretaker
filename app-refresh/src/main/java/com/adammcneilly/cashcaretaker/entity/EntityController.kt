package com.adammcneilly.cashcaretaker.entity

import com.adammcneilly.cashcaretaker.core.DataController

/**
 * Base view for displaying a list of entities.
 */
interface EntityController<in T>: DataController {
    fun showEntities(entities: List<T>)
}