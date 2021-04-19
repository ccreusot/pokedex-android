package fr.cedriccreusot.pokedex.dataadapter

import fr.cedriccreusot.domain.common.model.NotFoundError
import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.common.repository.PokemonDetailRepository
import fr.cedriccreusot.domain.detail.model.PokemonDetail
import fr.cedriccreusot.domain.detail.model.PokemonStats
import me.sargunvohra.lib.pokekotlin.client.PokeApi
import javax.inject.Inject

class PokemonDetailRepositoryAdapter @Inject constructor(
    private val pokemonDataSource: PokeApi
) : PokemonDetailRepository {
    override fun getPokemon(id: Int): Result<PokemonDetail> {
        lateinit var result: Result<PokemonDetail>
        return runCatching {
            pokemonDataSource.getPokemon(id)
        }.let {
            result = it.getOrNull()?.let {
                Success(PokemonDetail(
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    emptyList(),
                    "" to "",
                    emptyList(),
                    "",
                    PokemonStats(0,0, 0, 0, 0, 0),
                    emptyMap(),
                    emptyList()
                ))
            } ?: NotFoundError(id)
            result
        }
    }
}
