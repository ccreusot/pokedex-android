import fr.cedriccreusot.pokedex.domain.common.model.EmptyError
import fr.cedriccreusot.pokedex.domain.list.model.Pokemon
import fr.cedriccreusot.pokedex.domain.common.model.Result
import fr.cedriccreusot.pokedex.domain.common.model.Success
import me.sargunvohra.lib.pokekotlin.client.PokeApi

class PokemonRepositoryAdapter(private val pokemonDataSource: PokeApi) : PokemonRepository {
    override fun getPokemons(): Result<List<Pokemon>> {
        lateinit var result: Result<List<Pokemon>>
        runCatching {
            pokemonDataSource.getPokemonList(0, 151)
        }.onSuccess {
            result = Success(it.results.map { pokemon ->
                Pokemon(pokemon.id, pokemon.name)
            })
        }.onFailure {
            result = EmptyError<List<Pokemon>>()
        }
        return result
    }
}