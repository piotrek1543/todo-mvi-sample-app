package com.piotrek1543.example.todoapp.data.model


import java.util.*


/**
 * Immutable model class for a CachedTask. In order to compile with Room, we can't use @JvmOverloads to
 * generate multiple constructors.
 *
 * @param title       title of the task
 * @param description description of the task
 * @param isCompleted whether or not this task is completed
 * @param id          id of the task
 */
data class Task @JvmOverloads constructor(
        var title: String = "",
        var description: String = "",
        var isCompleted: Boolean = false,
        var id: String = UUID.randomUUID().toString()
) {

    val titleForList: String
        get() = if (title.isNotEmpty()) title else description


    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()
}