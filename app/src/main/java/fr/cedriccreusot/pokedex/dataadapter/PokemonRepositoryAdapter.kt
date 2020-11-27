package fr.cedriccreusot.pokedex.dataadapter

import androidx.paging.PagingSource
import fr.cedriccreusot.domain.common.model.EmptyError
import fr.cedriccreusot.domain.common.model.Error
import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.domain.list.repository.PokemonRepository
import me.sargunvohra.lib.pokekotlin.client.PokeApi
import javax.inject.Inject

class PokemonRepositoryAdapter @Inject constructor(
    private val pokemonDataSource: PokeApi
) : PokemonRepository {
    override fun getPokemons(offset: Int, limit: Int): Result<List<Pokemon>> {
        lateinit var result: Result<List<Pokemon>>
        runCatching {
            pokemonDataSource.getPokemonList(offset, limit)
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
}

class PokemonsPagingSource(private val repository: PokemonRepository) : PagingSource<Int, Pokemon>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val askedPage = params.key ?: 0
        val result = repository.getPokemons(askedPage * OFFSET, OFFSET)
        return when (result) {
            is Success -> LoadResult.Page(data=result.value, prevKey = null, nextKey = askedPage + 1)
            is Error -> LoadResult.Error(Exception(result.message))
        }
    }
}

private const val OFFSET = 20
