package fr.cedriccreusot.domain.list.repository

import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.list.model.Pokemon

interface PokemonRepository {
    fun getPokemons(page: Int): Result<List<Pokemon>>
}