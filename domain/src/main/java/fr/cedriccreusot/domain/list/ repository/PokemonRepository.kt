import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.domain.common.model.Result

interface PokemonRepository {
    fun getPokemons() : Result<List<Pokemon>>
}