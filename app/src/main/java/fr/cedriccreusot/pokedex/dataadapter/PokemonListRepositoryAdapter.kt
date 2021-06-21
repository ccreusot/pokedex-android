package fr.cedriccreusot.pokedex.dataadapter

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import fr.cedriccreusot.domain.common.model.EmptyError
import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.common.repository.PokemonListRepository
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.pokedex.PokemonListQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonListRepositoryAdapter @Inject constructor(
    private val pokemonDataSource: ApolloClient
) : PokemonListRepository {
    override fun getPokemons(page: Int): Result<List<Pokemon>> {
        lateinit var result: Result<List<Pokemon>>
        runCatching {
            runBlocking {
                withContext(Dispatchers.IO) {
                    pokemonDataSource
                        .query(PokemonListQuery(Input.fromNullable(page * LIMIT)))
                        .await()
                }
            }
            //pokemonDataSource.getPokemonList(page * LIMIT, LIMIT)
        }.onSuccess {
//            TODO("find a solution for missing sprites")
            result =
                Success(it.data!!.pokemon_v2_pokemon.map { pokemon ->
                    Pokemon(
                        pokemon.id,
                        pokemon.name,
                        "",
                        pokemon.pokemon_v2_pokemontypes.firstOrNull()?.pokemon_v2_type?.name ?: "",
                        pokemon.pokemon_v2_pokemontypes.getOrNull(1)?.pokemon_v2_type?.name ?: "",
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
