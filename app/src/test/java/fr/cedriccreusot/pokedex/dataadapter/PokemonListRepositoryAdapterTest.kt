package fr.cedriccreusot.pokedex.dataadapter

import com.google.common.truth.Truth.assertThat
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.pokedex.PokemonListQuery
import fr.cedriccreusot.pokedex.datasource.PokemonDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PokemonListRepositoryAdapterTest {
    private lateinit var dataSource: PokemonDataSource
    private lateinit var repository: PokemonListRepositoryAdapter

    @Before
    fun setUp() {
        dataSource = mockk()
        repository = PokemonListRepositoryAdapter(dataSource)
    }

    @Test
    fun `when repository call the pokemon api for the first pokedex page then it should return a Success with the list of pokemon`() =
        runBlockingTest {
            // Given
            val mockedResponse = mockk<PokemonListQuery.Data>()
            val mockedPokemon = mockk<PokemonListQuery.Pokemon_v2_pokemon>()
            coEvery { dataSource.queryPokemons(any()) } returns Success(mockedResponse)
            every { mockedResponse.pokemon_v2_pokemon } returns listOf(mockedPokemon)
            every { mockedPokemon.name } returns ""
            every { mockedPokemon.id } returns 0
            every { mockedPokemon.pokemon_v2_pokemontypes } returns emptyList()

            // When
            val result = repository.getPokemons(0)

            // Then
            coVerify { dataSource.queryPokemons(any()) }
            assertThat(result is Success<List<Pokemon>>).isTrue()
        }
/*
    @Test
    fun `when repository call the pokemon api for the 2nd pokedex page it should return a concatenated list of page 01 and page 02`() {
        every { dataSource.getPokemon(any()) } returns mockedPokemon()
        every { dataSource.getPokemonList(PAGE, LIMIT) }.returns(
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
        every { dataSource.getPokemonList(PAGE2, LIMIT) }.returns(
            NamedApiResourceList(
                count = 3,
                next = null,
                previous = null,
                results = listOf(
                    NamedApiResource(name = "Cedric", category = "normal", id = 4),
                    NamedApiResource(name = "JC", category = "normal", id = 5),
                    NamedApiResource(name = "Sylvain", category = "normal", id = 6),
                    NamedApiResource(name = "Benjamin", category = "normal", id = 7),
                )
            )
        )
        repository.getPokemons(PAGE)
        val results = repository.getPokemons(1)

        verify { dataSource.getPokemonList(PAGE, LIMIT) }
        verify { dataSource.getPokemonList(PAGE2, LIMIT) }
        verify { dataSource.getPokemon(any()) }
        confirmVerified(dataSource)
        assertThat(results is Success<List<Pokemon>>).isTrue()
        assertThat((results as Success).value.size).isEqualTo(4)
    }

    @Test
    fun `when repository call the pokemon api for the pokemon list which will throw an exception then it should return an Error`() {
        every { dataSource.getPokemonList(any(), any()) }.throws(
            ErrorResponse(
                404,
                "Error not found"
            )
        )

        val result = repository.getPokemons(PAGE)

        verify { dataSource.getPokemonList(PAGE, LIMIT) }
        confirmVerified(dataSource)
        assertThat(result is EmptyError<List<Pokemon>>).isTrue()
    }

    companion object {
        const val PAGE: Int = 0
        const val PAGE2: Int = 20
        const val LIMIT: Int = 20
    }

 */
}