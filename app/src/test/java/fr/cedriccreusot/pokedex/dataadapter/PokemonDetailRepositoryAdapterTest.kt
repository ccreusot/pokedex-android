package fr.cedriccreusot.pokedex.dataadapter

import com.google.common.truth.Truth.assertThat
import fr.cedriccreusot.domain.common.model.NotFoundError
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.detail.model.Move
import fr.cedriccreusot.domain.detail.model.PokemonDetail
import fr.cedriccreusot.domain.detail.model.PokemonStats
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
    fun `when get Pokemon from data source then should transform it to Pokemon detail and return into Success `() {
        val pokemonId = 42
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
            stats = listOf(
                PokemonStat(NamedApiResource("hp", "stat",id = 1), effort = 0, baseStat = 75),
                PokemonStat(NamedApiResource("attack", "stat", id = 1), effort = 0, baseStat = 80),
                PokemonStat(NamedApiResource("defense", "stat", id = 1), effort = 0, baseStat = 80),
                PokemonStat(NamedApiResource("spAttack", "stat", id = 1), effort = 0, baseStat = 80),
                PokemonStat(NamedApiResource("spDefense", "stat", id = 1), effort = 0, baseStat = 80),
                PokemonStat(NamedApiResource("speed", "stat", id = 1), effort = 0, baseStat = 80),

                ),
            types = listOf(
                PokemonType(1, NamedApiResource("poison", "type", 4)),
                PokemonType(2, NamedApiResource("flying", "type", 3))
            ),
            sprites = PokemonSprites("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/42.png", null, "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/42.png", null, null, null, null, null)
        )

        val pokemonDetail = PokemonDetail(
            id = 42,
            name = "Golbat",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/42.png",
            mainType = "poison",
            secondaryType = "flying",
            species = "golbat",
            height = 16,
            weight = 550,
            abilities = listOf(
                "inner-focus", "infiltrator"
            ),
            stats = PokemonStats(
                hp = 75,
                attack = 80,
                defense = 80,
                spAttack = 80,
                spDefense = 80,
                speed = 80
            ),
            moves = listOf(
                "wind-attack",
            )
        )

        // Given
        every { pokeApi.getPokemon(pokemonId) } returns pokemon

        // When
        val result = repository.getPokemon(pokemonId)

        // Then
        assertThat(result).isInstanceOf(Success::class.java)
        assertThat((result as Success).value).isInstanceOf(PokemonDetail::class.java)
        assertThat(result.value).isEqualTo(pokemonDetail)
    }
}
