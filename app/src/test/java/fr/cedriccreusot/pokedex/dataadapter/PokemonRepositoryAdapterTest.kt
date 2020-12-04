package fr.cedriccreusot.pokedex.dataadapter

import com.google.common.truth.Truth.assertThat
import fr.cedriccreusot.domain.common.model.EmptyError
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.list.model.Pokemon
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.sargunvohra.lib.pokekotlin.client.ErrorResponse
import me.sargunvohra.lib.pokekotlin.client.PokeApi
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource
import me.sargunvohra.lib.pokekotlin.model.NamedApiResourceList
import me.sargunvohra.lib.pokekotlin.model.PokemonSprites
import me.sargunvohra.lib.pokekotlin.model.PokemonType
import org.junit.Test

typealias MockedPokemon = me.sargunvohra.lib.pokekotlin.model.Pokemon

class PokemonRepositoryAdapterTest {
    @Test
    fun `when repository call the pokemon api for the first pokedex page then it should return a Success with the list of pokemon`() {
        val pokeapi = mockk<PokeApi>()
        every {
            pokeapi.getPokemon(any())
        }.returns(
            MockedPokemon(
                0,
                "nyanCat",
                0,
                0,
                true,
                0,
                0,
                NamedApiResource("", "", 0),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                listOf(PokemonType(0, NamedApiResource("Fire", "Cat", 0))),
                PokemonSprites(null, null, null, null, null, null, null, null)
            )
        )
        every { pokeapi.getPokemonList(PAGE, LIMIT) }.returns(
            NamedApiResourceList(
                count = 3,
                next = null,
                previous = null,
                results = listOf(
                    NamedApiResource(name = "Cedric", category = "normal", id = 0),
                    NamedApiResource(name = "JC", category = "normal", id = 1),
                    NamedApiResource(name = "Sylvain", category = "normal", id = 2),
                    NamedApiResource(name = "Benjamin", category = "normal", id = 3),
                )
            )
        )
        val repository = PokemonRepositoryAdapter(pokeapi)

        val result = repository.getPokemons(PAGE)

        verify { pokeapi.getPokemonList(PAGE, LIMIT) }
        verify { pokeapi.getPokemon(any()) }
        confirmVerified(pokeapi)
        assertThat(result is Success<List<Pokemon>>).isTrue()
    }

    @Test
    fun `when repository call the pokemon api for the 2nd pokedex page it should return a concatenated list of page 01 and page 02`() {
        TODO("Not yet implemented")
    }

    @Test
    fun `when repository call the pokemon api for the pokemon list which will throw an exception then it should return an Error`() {
        val pokeapi = mockk<PokeApi>()

        every { pokeapi.getPokemonList(any(), any()) }.throws(ErrorResponse(404, "Error not found"))

        val repository = PokemonRepositoryAdapter(pokeapi)

        val result = repository.getPokemons(PAGE)

        verify { pokeapi.getPokemonList(PAGE, LIMIT) }
        confirmVerified(pokeapi)
        assertThat(result is EmptyError<List<Pokemon>>).isTrue()
    }

    companion object {
        const val PAGE: Int = 0
        const val LIMIT: Int = 20
    }
}