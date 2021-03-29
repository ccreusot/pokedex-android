package fr.cedriccreusot.pokedex.dataadapter

import com.google.common.truth.Truth.assertThat
import fr.cedriccreusot.domain.common.model.NotFoundError
import io.mockk.every
import io.mockk.mockk
import me.sargunvohra.lib.pokekotlin.client.ErrorResponse
import me.sargunvohra.lib.pokekotlin.client.PokeApi

import org.junit.Before
import org.junit.Test

class PokemonDetailRepositoryAdapterTest {

    private lateinit var pokeApi: PokeApi
    private lateinit var repository: PokemonDetailRepositoryAdapter

    @Before
    fun setUp() {
        pokeApi = mockk()
        repository = PokemonDetailRepositoryAdapter(pokeApi)
    }

    @Test
    fun `when poke api called and response is a failure then repository should return an Error`() {
        // Given
        every { pokeApi.getPokemon(any()) }.throws(ErrorResponse(404, "Error not found"))

        // When
        val result = repository.getPokemon(0)

        // Then
        assertThat(result).isInstanceOf(NotFoundError::class.java)
    }
}