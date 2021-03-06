package com.piotrek1543.example.todoapp.remote.mapper

/**
 * Interface for model mappers. It provides helper methods that facilitate
 * retrieving of models from outer data source layers
 *
 * @param <M> the remote model input type
 * @param <E> the entity model output type
 */
interface EntityMapper<T, V> {

    fun mapFromRemote(type: T): V

    fun mapToRemote(type: V): T

}