package com.androidessence.cashcaretaker.entity

import com.androidessence.cashcaretaker.core.DataController

/**
 * Base view for displaying a list of entities.
 */
interface EntityController<in T>: DataController {
    fun showEntities(entities: List<T>)
}