package fr.cedriccreusot.domain.list.usecase

import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.domain.common.repository.PokemonRepository

interface FetchPokemonListUseCase {
    operator fun invoke(page: Int): Result<List<Pokemon>>

    companion object {
        fun create(repository: PokemonRepository): FetchPokemonListUseCase =
            FetchPokemonListUseCaseImpl(repository)
    }
}

internal class FetchPokemonListUseCaseImpl constructor(
    private val repository: PokemonRepository
) : FetchPokemonListUseCase {

    override operator fun invoke(page: Int): Result<List<Pokemon>> =
        repository.getPokemons(page)
}