package fr.cedriccreusot.pokedex.dataadapter

import com.apollographql.apollo.ApolloClient
import fr.cedriccreusot.domain.common.model.NotFoundError
import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.common.repository.PokemonDetailRepository
import fr.cedriccreusot.domain.detail.model.PokemonDetail
import javax.inject.Inject

class PokemonDetailRepositoryAdapter @Inject constructor(
    private val pokemonDataSource: ApolloClient
) : PokemonDetailRepository {
    override fun getPokemon(id: Int): Result<PokemonDetail> {
        return runCatching {
            //pokemonDataSource.getPokemon(id)
        }.let { result ->
            /*result.getOrNull()?.let { pokemon ->
                Success(
                    PokemonDetail(
                        id = pokemon.id,
                        name = pokemon.name,
                        imageUrl = pokemon.sprites.frontDefault ?: "",
                        mainType = pokemon.types.first().type.name,
                        secondaryType = pokemon.types.let { types ->
                            if (types.size > 1) {
                                return@let types[1].type.name
                            }
                            null
                        },
                        species = pokemon.species.name,
                        height = pokemon.height,
                        weight = pokemon.weight,
                        abilities = pokemon.abilities.map { ability ->
                            ability.ability.name
                        },
                        stats = PokemonStats(
                            hp = pokemon.stats.firstOrNull { stat -> stat.stat.name == "hp" }?.baseStat ?: 0,
                            attack = pokemon.stats.firstOrNull { state -> state.stat.name == "attack" }?.baseStat ?: 0,
                            defense = pokemon.stats.firstOrNull { state -> state.stat.name == "defense" }?.baseStat ?: 0,
                            spAttack = pokemon.stats.firstOrNull { state -> state.stat.name == "spAttack"}?.baseStat ?: 0,
                            spDefense = pokemon.stats.firstOrNull { state -> state.stat.name == "spDefense" }?.baseStat ?: 0,
                            speed = pokemon.stats.firstOrNull { state -> state.stat.name == "speed" }?.baseStat ?: 0
                        ),
                        moves = pokemon.moves.map { move ->
                            move.move.name
                        }
                    )
                )
            } ?: NotFoundError(id)*/
            NotFoundError(id)
        }
    }
}
