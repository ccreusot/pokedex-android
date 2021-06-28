package fr.cedriccreusot.pokedex.datasource

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.pokedex.PokemonListQuery
import javax.inject.Inject

class PokemonDataSourceImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : PokemonDataSource {
    override suspend fun queryPokemons(page: Int): Result<Any> = apolloClient
        .query(PokemonListQuery(Input.fromNullable(page * LIMIT)))
        .await()
        .let { Success(it.data!!) }

    companion object {
        private const val LIMIT = 20
    }
}