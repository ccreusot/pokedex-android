package fr.cedriccreusot.pokedex.presentation.list

import androidx.lifecycle.Observer
import fr.cedriccreusot.domain.common.model.EmptyError
import fr.cedriccreusot.domain.common.model.PageEndOfPages
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.domain.list.usecase.FetchPokemonListUseCase
import fr.cedriccreusot.pokedex.presentation.list.PokemonListViewModel.State
import fr.cedriccreusot.pokedex.utils.ViewModelTest
import io.mockk.*
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class PokemonListViewModelTest : ViewModelTest() {

    private lateinit var useCase: FetchPokemonListUseCase

    @Before
    override fun setUp() {
        super.setUp()
        useCase = mockk()
    }

    @Test
    fun `when we fetch the pokemonList then we should retrieve it`() {
        val pokemonList = listOf(
            Pokemon(0, "Zero", "imageUrl", "Fire", null)
        )
        coEvery { useCase.invoke(0) }.returns(
            Success(
                pokemonList
            )
        )
        val observe = mockk<Observer<State>>(relaxed = true)

        val viewModel = PokemonListViewModel(useCase)

        viewModel.pokemonListState().observeForever(observe)
        viewModel.fetchPokemons()

        verify(atMost = 2) { observe.onChanged(State.Loading) }
        verify { observe.onChanged(State.Success(pokemonList)) }
        confirmVerified(observe)
    }

    @Test
    fun `when we fetch the pokemonList and its already loaded it should not return the same list`() {
        val pokemonList = listOf(
            Pokemon(0, "Zero", "imageUrl", "Fire", null)
        )
        coEvery { useCase.invoke(0) }.returns(
            Success(
                pokemonList
            )
        )
        val observe = mockk<Observer<State>>(relaxed = true)

        val viewModel = PokemonListViewModel(useCase)

        viewModel.pokemonListState().observeForever(observe)
        viewModel.fetchPokemons()
        viewModel.fetchPokemons()

        verify(atMost = 3) { observe.onChanged(State.Loading) }
        verify(atMost = 2) { observe.onChanged(State.Success(pokemonList)) }
        confirmVerified(observe)
    }

    @Test
    fun `when we want the next page it should refresh the pokemon list with the next elements`() =
        runBlockingTest {
            val firstPage = listOf(
                Pokemon(0, "Zero", "imageUrl", "Fire", null)
            )
            val nextPage = listOf(
                Pokemon(1, "TheONE", "imageUrl", "THEONE", null)
            )
            val nextPage2 = listOf(
                Pokemon(2, "TheSecond", "imageUrl", "THEONE", null)
            )
            coEvery {
                useCase.invoke(0)
            }.returns(Success(firstPage))
            coEvery {
                useCase.invoke(1)
            }.returns(Success(nextPage))
            coEvery {
                useCase.invoke(2)
            }.returns(Success(nextPage2))

            val observe = mockk<Observer<State>>(relaxed = true)

            val viewModel = PokemonListViewModel(useCase)

            viewModel.pokemonListState().observeForever(observe)
            viewModel.fetchPokemons()
            // Cause LiveData observer
            // We need to put a delay of 1 millis to help the Mocked observer fetch the Data...
            delay(100)
            viewModel.nextPage()
            viewModel.nextPage()

            verify(atMost = 2) { observe.onChanged(State.Loading) }
            verify(atMost = 2) { observe.onChanged(State.LoadingNextPage) }
            verify { observe.onChanged(State.Success(firstPage)) }
            verify { observe.onChanged(State.Success(firstPage + nextPage)) }
            verify { observe.onChanged(State.Success(firstPage + nextPage + nextPage2)) }
            confirmVerified(observe)
        }

    @Test
    fun `when we want the next page but we are already at the last page it should not refresh the pokemon list with the next elements`() =
        runBlockingTest {
            val firstPage = listOf(
                Pokemon(0, "Zero", "imageUrl", "Fire", null)
            )
            coEvery {
                useCase.invoke(0)
            }.returns(Success(firstPage))
            coEvery {
                useCase.invoke(1)
            }.returns(PageEndOfPages())

            val observe = mockk<Observer<State>>(relaxed = true)

            val viewModel = PokemonListViewModel(useCase)

            viewModel.pokemonListState().observeForever(observe)
            viewModel.fetchPokemons()
            viewModel.nextPage()

            verify(atMost = 2) { observe.onChanged(State.Loading) }
            verify(exactly = 1) { observe.onChanged(State.LoadingNextPage) }
            verify { observe.onChanged(State.Success(firstPage)) }
            verify { observe.onChanged(State.Error("page end of pages")) }

            confirmVerified(observe)
        }

    @Test
    fun `when we observe the pokemonList and the usecase return an error it should return an error state`() {
        coEvery { useCase.invoke(any()) }.returns(
            EmptyError()
        )
        val observe = mockk<Observer<State>>(relaxed = true)

        val viewModel = PokemonListViewModel(useCase)

        viewModel.pokemonListState().observeForever(observe)
        viewModel.fetchPokemons()

        verify(atMost = 2) { observe.onChanged(State.Loading) }
        verify { observe.onChanged(State.Error("Empty Pokemon list")) }
        confirmVerified(observe)
    }
}
