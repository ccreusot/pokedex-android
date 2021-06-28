package fr.cedriccreusot.pokedex.dataadapter

import fr.cedriccreusot.domain.common.model.EmptyError
import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.common.repository.PokemonListRepository
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.pokedex.PokemonListQuery
import fr.cedriccreusot.pokedex.datasource.PokemonDataSource
import javax.inject.Inject

class PokemonListRepositoryAdapter @Inject constructor(
    private val pokemonDataSource: PokemonDataSource
) : PokemonListRepository {
    override suspend fun getPokemons(page: Int): Result<List<Pokemon>> {
        lateinit var result: Result<List<Pokemon>>
        runCatching {
            pokemonDataSource.queryPokemons(page * LIMIT)
        }.onSuccess {
            result =
                Success(((it as Success).value as PokemonListQuery.Data).pokemon_v2_pokemon.map { pokemon ->
                    Pokemon(
                        id = pokemon.id,
                        name = pokemon.name,
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.id}.png",
                        mainType = pokemon.pokemon_v2_pokemontypes.firstOrNull()?.pokemon_v2_type?.name
                            ?: "",
                        secondaryType = pokemon.pokemon_v2_pokemontypes.getOrNull(1)?.pokemon_v2_type?.name,
                    )
                })
        }.onFailure {
            result = EmptyError()
        }
        return result
    }

    companion object {
        private const val LIMIT = 20
    }
}
