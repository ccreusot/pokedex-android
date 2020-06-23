package fr.cedriccreusot.domain.common.model

sealed class Result<T>

sealed class Error<T>(val message: String): Result<T>()

class Success<T>(val value : T) : Result<T>()

class EmptyError<T>: Error<T>("Empty Pokemon list")