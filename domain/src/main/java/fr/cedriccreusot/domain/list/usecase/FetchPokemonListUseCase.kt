package fr.cedriccreusot.domain.list.usecase

import PokemonRepository
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.domain.common.model.Result

interface FetchPokemonListUseCase {
    operator fun invoke(): Result<List<Pokemon>>

    companion object {
        fun create(repository: PokemonRepository) : FetchPokemonListUseCase =
            FetchPokemonListUseCaseImpl(
                repository
            )
    }
}

internal class FetchPokemonListUseCaseImpl(private val repository : PokemonRepository):
    FetchPokemonListUseCase {

    override operator fun invoke(): Result<List<Pokemon>> = repository.getPokemons()
}