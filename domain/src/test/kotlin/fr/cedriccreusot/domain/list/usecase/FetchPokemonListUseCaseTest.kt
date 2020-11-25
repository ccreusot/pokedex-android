package fr.cedriccreusot.domain.list.usecase

import com.google.common.truth.Truth.assertThat
import fr.cedriccreusot.domain.common.model.EmptyError
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.domain.list.repository.PokemonRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class FetchPokemonListUseCaseImplTest {

    private val repository: PokemonRepository = mockk()
    private val useCase: FetchPokemonListUseCase = FetchPokemonListUseCaseImpl(repository)

    @Test
    fun `when we fetch pokemon list we should have a list filled with pokemon`() {
        every { repository.getPokemons(any(), any()) }.returns(
            Success(
                listOf(
                    Pokemon(0, "Mew", "", "Psy", null),
                    Pokemon(1, "Bulbizarre", "", "Grass", "Poison"),
                    Pokemon(2, "Sylvain", "", "Developer", "ADSL")
                )
            )
        )

        val result = useCase(0, Int.MAX_VALUE)

        verify { repository.getPokemons(any(), any()) }
        confirmVerified(repository)

        assertThat((result as Success<List<Pokemon>>).value).isNotEmpty()
    }

    @Test
    fun `when we fetch pokemon list and the repository failed we should return an Error with error message`() {
        every { repository.getPokemons(any(), any()) }.returns(EmptyError())

        val result = useCase(0, Int.MAX_VALUE)

        verify { repository.getPokemons(any(), any()) }
        assertThat(result).isInstanceOf(EmptyError::class.java)
        confirmVerified(repository)
    }
}