package fr.cedriccreusot.pokedex.domain.list.usecase

import PokemonRepository
import com.google.common.truth.Truth.*
import fr.cedriccreusot.pokedex.domain.common.model.EmptyError
import fr.cedriccreusot.pokedex.domain.common.model.Success
import fr.cedriccreusot.pokedex.domain.list.model.Pokemon
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class FetchPokemonListUseCaseImplTest {
    @Test
    fun `when we fetch pokemon list we should have a list filled with pokemon`() {
        val repository = mockk<PokemonRepository>()
        every { repository.getPokemons() }.returns(
            Success(listOf(
            Pokemon(0, "Mew"),
            Pokemon(1, "Bulbizar"),
            Pokemon(2, "Sylvain")
        ))
        )
        val usecase = FetchPokemonListUseCaseImpl(repository)

        val result = usecase()

        verify { repository.getPokemons() }
        confirmVerified(repository)

        assertThat((result as Success<List<Pokemon>>).value).isNotEmpty()
    }

    @Test
    fun `when we fetch pokemon list and the repository failed we should return an Error with error message`() {
        val repository = mockk<PokemonRepository>()
        every { repository.getPokemons() }.returns(EmptyError())
        val usecase = FetchPokemonListUseCaseImpl(repository)

        val result = usecase()

        verify { repository.getPokemons() }
        assertThat(result).isInstanceOf(EmptyError::class.java)
        confirmVerified(repository)
    }
}