package fr.cedriccreusot.domain.list.usecase

import com.google.common.truth.Truth.assertThat
import fr.cedriccreusot.domain.common.model.EmptyError
import fr.cedriccreusot.domain.common.model.PageEndOfPages
import fr.cedriccreusot.domain.common.model.PageInvalidIndex
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.common.repository.PokemonListRepository
import fr.cedriccreusot.domain.list.model.Pokemon
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class FetchPokemonListUseCaseImplTest {

    private val repository: PokemonListRepository = mockk()
    private val useCase: FetchPokemonListUseCase = FetchPokemonListUseCaseImpl(repository)

    @Test
    fun `when we fetch a pokedex page we should have a list filled with pokemon`() = runBlockingTest {
        coEvery { repository.getPokemons(page = any()) }.returns(
            Success(
                listOf(
                    Pokemon(0, "Mew", "", "Psy", null),
                    Pokemon(1, "Bulbizarre", "", "Grass", "Poison"),
                    Pokemon(2, "Sylvain", "", "Developer", "ADSL")
                )
            )
        )

        val result = useCase(page = 0)

        coVerify { repository.getPokemons(page = any()) }
        confirmVerified(repository)

        assertThat((result as Success<List<Pokemon>>).value).isNotEmpty()
    }

    @Test
    fun `when we fetch a pokedex page and the repository failed we should return an Error with error message`()  = runBlockingTest {
        coEvery { repository.getPokemons(page = any()) }.returns(EmptyError())

        val result = useCase(page = 0)

        coVerify { repository.getPokemons(page = any()) }
        assertThat(result).isInstanceOf(EmptyError::class.java)
        confirmVerified(repository)
    }

    @Test
    fun `when we fetch a pokedex page with an invalid index we should get a PageInvalidIndex`()  = runBlockingTest {
        coEvery { repository.getPokemons(page = -1) }.returns(PageInvalidIndex(-1))

        val result = useCase(page = -1)

        coVerify { repository.getPokemons(page = -1) }
        assertThat(result).isInstanceOf(PageInvalidIndex::class.java)
        confirmVerified(repository)
    }

    @Test
    fun `when we fetch a pokedex page and the last one was already done we should get a PageEndOfPages`()  = runBlockingTest {
        coEvery { repository.getPokemons(page = any()) }.returns(PageEndOfPages())

        val result = useCase(page = Int.MAX_VALUE)

        coVerify { repository.getPokemons(page = any()) }
        assertThat(result).isInstanceOf(PageEndOfPages::class.java)
        confirmVerified(repository)
    }

}