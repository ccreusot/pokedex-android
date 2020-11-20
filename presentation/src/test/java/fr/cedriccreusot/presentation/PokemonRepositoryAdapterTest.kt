package fr.cedriccreusot.presentation

import PokemonRepositoryAdapter
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
import org.junit.Test

class PokemonRepositoryAdapterTest {
    @Test
    fun `when repository call the pokemon api for the pokemon list then it should return a Success with the list of pokemon`() {
        val pokeapi = mockk<PokeApi>()
        every { pokeapi.getPokemonList(0, 151) }.returns(
            NamedApiResourceList(
                count = 3,
                next = null,
                previous = null,
                results = listOf(
                    NamedApiResource(name = "Cedric", category = "normal", id = 0),
                    NamedApiResource(name = "JC", category = "normal", id = 1),
                    NamedApiResource(name = "Sylvain", category = "normal", id = 2)
                )
            )
        )
        val repository = PokemonRepositoryAdapter(pokeapi)

        val result = repository.getPokemons()

        verify { pokeapi.getPokemonList(0, 151) }
        confirmVerified(pokeapi)
        assertThat(result is Success<List<Pokemon>>).isTrue()
    }

    @Test
    fun `when repository call the pokemon api for the pokemon list which will throw an exception then it should return an Error`() {
        val pokeapi = mockk<PokeApi>()

        every { pokeapi.getPokemonList(0, 151) }.throws(ErrorResponse(404, "Error not found"))

        val repository = PokemonRepositoryAdapter(pokeapi)

        val result = repository.getPokemons()

        verify { pokeapi.getPokemonList(0, 151) }
        confirmVerified(pokeapi)
        assertThat(result is EmptyError<List<Pokemon>>).isTrue()
    }
}