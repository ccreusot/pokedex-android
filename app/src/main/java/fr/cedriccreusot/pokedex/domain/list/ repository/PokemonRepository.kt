import fr.cedriccreusot.pokedex.domain.list.model.Pokemon
import fr.cedriccreusot.pokedex.domain.common.model.Result

interface PokemonRepository {
    fun getPokemons() : Result<List<Pokemon>>
}