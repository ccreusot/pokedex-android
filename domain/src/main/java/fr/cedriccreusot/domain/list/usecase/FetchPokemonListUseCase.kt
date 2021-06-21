package fr.cedriccreusot.domain.list.usecase

import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.common.repository.PokemonListRepository
import fr.cedriccreusot.domain.list.model.Pokemon

interface FetchPokemonListUseCase {
    suspend operator fun invoke(page: Int): Result<List<Pokemon>>

    companion object {
        fun create(repository: PokemonListRepository): FetchPokemonListUseCase =
            FetchPokemonListUseCaseImpl(repository)
    }
}

internal class FetchPokemonListUseCaseImpl constructor(
    private val repository: PokemonListRepository
) : FetchPokemonListUseCase {

    override suspend operator fun invoke(page: Int): Result<List<Pokemon>> =
        repository.getPokemons(page)
}