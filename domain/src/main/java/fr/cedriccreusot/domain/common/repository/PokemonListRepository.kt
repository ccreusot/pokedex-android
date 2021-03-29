package fr.cedriccreusot.domain.common.repository

import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.list.model.Pokemon

interface PokemonListRepository {
    fun getPokemons(page: Int): Result<List<Pokemon>>
}