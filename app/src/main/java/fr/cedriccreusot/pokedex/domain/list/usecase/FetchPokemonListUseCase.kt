package fr.cedriccreusot.pokedex.domain.list.usecase

import PokemonRepository
import fr.cedriccreusot.pokedex.domain.list.model.Pokemon
import fr.cedriccreusot.pokedex.domain.common.model.Result

interface FetchPokemonListUseCase {
    operator fun invoke(): Result<List<Pokemon>>
}

internal class FetchPokemonListUseCaseImpl(private val repository : PokemonRepository): FetchPokemonListUseCase {

    override operator fun invoke(): Result<List<Pokemon>> = repository.getPokemons()
}