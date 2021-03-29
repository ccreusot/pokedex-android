package fr.cedriccreusot.domain.list.usecase

import com.google.common.truth.Truth.assertThat
import fr.cedriccreusot.domain.common.model.EmptyError
import fr.cedriccreusot.domain.common.model.PageEndOfPages
import fr.cedriccreusot.domain.common.model.PageInvalidIndex
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.common.repository.PokemonListRepository
import fr.cedriccreusot.domain.list.model.Pokemon
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class FetchPokemonListUseCaseImplTest {

    private val repository: PokemonListRepository = mockk()
    private val useCase: FetchPokemonListUseCase = FetchPokemonListUseCaseImpl(repository)

    @Test
    fun `when we fetch a pokedex page we should have a list filled with pokemon`() {
        every { repository.getPokemons(page = any()) }.returns(
            Success(
                listOf(
                    Pokemon(0, "Mew", "", "Psy", null),
                    Pokemon(1, "Bulbizarre", "", "Grass", "Poison"),
                    Pokemon(2, "Sylvain", "", "Developer", "ADSL")
                )
            )
        )

        val result = useCase(page = 0)

        verify { repository.getPokemons(page = any()) }
        confirmVerified(repository)

        assertThat((result as Success<List<Pokemon>>).value).isNotEmpty()
    }

    @Test
    fun `when we fetch a pokedex page and the repository failed we should return an Error with error message`() {
        every { repository.getPokemons(page = any()) }.returns(EmptyError())

        val result = useCase(page = 0)

        verify { repository.getPokemons(page = any()) }
        assertThat(result).isInstanceOf(EmptyError::class.java)
        confirmVerified(repository)
    }

    @Test
    fun `when we fetch a pokedex page with an invalid index we should get a PageInvalidIndex`() {
        every { repository.getPokemons(page = -1) }.returns(PageInvalidIndex(-1))

        val result = useCase(page = -1)

        verify { repository.getPokemons(page = -1) }
        assertThat(result).isInstanceOf(PageInvalidIndex::class.java)
        confirmVerified(repository)
    }

    @Test
    fun `when we fetch a pokedex page and the last one was already done we should get a PageEndOfPages`() {
        every { repository.getPokemons(page = any()) }.returns(PageEndOfPages())

        val result = useCase(page = Int.MAX_VALUE)

        verify { repository.getPokemons(page = any()) }
        assertThat(result).isInstanceOf(PageEndOfPages::class.java)
        confirmVerified(repository)
    }

}