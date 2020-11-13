import fr.cedriccreusot.domain.common.model.EmptyError
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.common.model.Success
import me.sargunvohra.lib.pokekotlin.client.PokeApi

class PokemonRepositoryAdapter(private val pokemonDataSource: PokeApi) : PokemonRepository {
    override fun getPokemons(): Result<List<Pokemon>> {
        lateinit var result: Result<List<Pokemon>>
        runCatching {
            pokemonDataSource.getPokemonList(0, 11)
        }.onSuccess {
            result =
                Success(it.results.map { resource ->
                    val pokemon = pokemonDataSource.getPokemon(resource.id)
                    Pokemon(
                        pokemon.id,
                        pokemon.name,
                        pokemon.sprites.frontDefault?:"",
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