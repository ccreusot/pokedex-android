package fr.cedriccreusot.domain.common.model

sealed class Result<T>

sealed class Error<T>(val message: String): Result<T>()

data class Success<T>(val value : T) : Result<T>()

class EmptyError<T> : Error<T>("Empty Pokemon list")

data class PageInvalidIndex<T>(val index: Int): Error<T>("Invalid index of $index")

class PageEndOfPages<T>: Error<T>("End of pages")
