package fr.cedriccreusot.domain.common.repository

import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.detail.model.PokemonDetail

interface PokemonDetailRepository {
    fun getPokemon(id: Int): Result<PokemonDetail>
}