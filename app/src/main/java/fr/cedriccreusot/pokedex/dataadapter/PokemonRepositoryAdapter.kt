package fr.cedriccreusot.pokedex.dataadapter

import fr.cedriccreusot.domain.common.model.EmptyError
import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.domain.list.repository.PokemonRepository
import me.sargunvohra.lib.pokekotlin.client.PokeApi
import javax.inject.Inject

class PokemonRepositoryAdapter @Inject constructor(
    private val pokemonDataSource: PokeApi
) : PokemonRepository {
    override fun getPokemons(page: Int): Result<List<Pokemon>> {
        lateinit var result: Result<List<Pokemon>>
        runCatching {
            pokemonDataSource.getPokemonList(page * LIMIT, LIMIT)
        }.onSuccess {
            result =
                Success(it.results.map { resource ->
                    val pokemon = pokemonDataSource.getPokemon(resource.id)
                    Pokemon(
                        pokemon.id,
                        pokemon.name,
                        pokemon.sprites.frontDefault ?: "",
                        pokemon.types[0].type.name,
                        pokemon.types.getOrNull(1)?.type?.name,
                    )
                })
        }.onFailure {
            result =
                EmptyError()
        }
        return result
    }

    companion object {
        private const val LIMIT = 20
    }
}