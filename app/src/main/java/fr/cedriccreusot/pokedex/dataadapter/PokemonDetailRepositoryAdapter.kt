package fr.cedriccreusot.pokedex.dataadapter

import fr.cedriccreusot.domain.common.model.NotFoundError
import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.common.repository.PokemonDetailRepository
import fr.cedriccreusot.domain.detail.model.PokemonDetail
import me.sargunvohra.lib.pokekotlin.client.PokeApi
import javax.inject.Inject

class PokemonDetailRepositoryAdapter @Inject constructor(
    private val pokemonDataSource: PokeApi
) : PokemonDetailRepository {
    override fun getPokemon(id: Int): Result<PokemonDetail> =
        runCatching {
            pokemonDataSource.getPokemon(id)
        }.let { NotFoundError(id) }
}