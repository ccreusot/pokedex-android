package fr.cedriccreusot.pokedex.dataadapter

import com.google.common.truth.Truth.assertThat
import fr.cedriccreusot.domain.common.model.NotFoundError
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.detail.model.PokemonDetail
import io.mockk.every
import io.mockk.mockk
import me.sargunvohra.lib.pokekotlin.client.ErrorResponse
import me.sargunvohra.lib.pokekotlin.client.PokeApi
import me.sargunvohra.lib.pokekotlin.model.*

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

    @Test
    fun `when poke api called and response is a success then repository should return a Success with a Pokemon detail`() {
        val pokemonId = 42

        // Given
        every { pokeApi.getPokemon(pokemonId) } returns mockk()

        // When
        val result = repository.getPokemon(pokemonId)

        // Then
        assertThat(result).isInstanceOf(Success::class.java)
        assertThat((result as Success).value).isInstanceOf(PokemonDetail::class.java)
    }

    @Test
    fun `when get Pokemon from data source then should transform it to Pokemon detail and return into Success `() {
        val pokemon = Pokemon(
            id = 42,
            name = "Golbat",
            baseExperience = 159,
            height = 16,
            isDefault = true,
            order = 72,
            weight = 550,
            species = NamedApiResource("golbat", "pokemon-species", id = 42),
            abilities = listOf(
                PokemonAbility(isHidden = false, slot = 1, NamedApiResource("inner-focus", "ability", id = 39)),
                PokemonAbility(isHidden = true, slot = 3, NamedApiResource("infiltrator", "ability", id = 151)),
            ),
            forms = listOf(NamedApiResource("golbat", "pokemon-form", id = 42)),
            gameIndices = emptyList(),
            heldItems = emptyList(),
            moves = listOf(PokemonMove(move = NamedApiResource("wind-attack", "move", 17), emptyList())),
            stats = listOf(PokemonStat(NamedApiResource("hp", "stat",id = 1), effort = 0, baseStat = 75)),
            types = listOf(
                PokemonType(1, NamedApiResource("poison", "type", 4)),
                PokemonType(2, NamedApiResource("flying", "type", 3))
            ),
            sprites = PokemonSprites("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/42.png", null, "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/42.png", null, null, null, null, null)
        )

        // Given
        TODO()
        // When


        // Then
    }
}
