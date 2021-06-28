package fr.cedriccreusot.pokedex.datasource

import fr.cedriccreusot.domain.common.model.Result

interface PokemonDataSource {
    suspend fun queryPokemons(page: Int): Result<Any>
}
