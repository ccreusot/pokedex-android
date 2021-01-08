package fr.cedriccreusot.pokedex.presentation.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import fr.cedriccreusot.domain.common.model.EmptyError
import fr.cedriccreusot.domain.common.model.PageEndOfPages
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.domain.list.usecase.FetchPokemonListUseCase
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class PokemonListViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var useCase: FetchPokemonListUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        useCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `when we observe the pokemonList it should start loading then return the list of pokemons`() {
        val pokemonList = listOf(
            Pokemon(0, "Zero", "imageUrl", "Fire", null)
        )
        every { useCase.invoke(any()) }.returns(
            Success(
                pokemonList
            )
        )
        val observe = mockk<Observer<State>>(relaxed = true)

        val viewModel = PokemonListViewModel(useCase)

        viewModel.pokemonListState().observeForever(observe)
        viewModel.fetchPokemons()

        verify { useCase.invoke(any()) }
        verify(atMost = 2) { observe.onChanged(State.Loading) }
        verify { observe.onChanged(State.Success(pokemonList)) }
        confirmVerified(useCase, observe)
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
            every {
                useCase.invoke(0)
            }.returns(Success(firstPage))
            every {
                useCase.invoke(1)
            }.returns(Success(nextPage))
            every {
                useCase.invoke(2)
            }.returns(Success(nextPage2))

            val observe = mockk<Observer<State>>(relaxed = true)

            val viewModel = PokemonListViewModel(useCase)

            viewModel.pokemonListState().observeForever(observe)
            viewModel.fetchPokemons()
            delay(100)
            viewModel.nextPage()
            viewModel.nextPage()

            verifySequence {
                observe.onChanged(State.Loading)
                useCase.invoke(any())
                observe.onChanged(State.Loading)
                observe.onChanged(State.Success(firstPage))
                useCase.invoke(any())
                observe.onChanged(State.LoadingNextPage)
                observe.onChanged(State.Success(firstPage + nextPage))
                useCase.invoke(any())
                observe.onChanged(State.LoadingNextPage)
                observe.onChanged(State.Success(firstPage + nextPage + nextPage2))
            }
            confirmVerified(useCase, observe)
        }

    @Test
    fun `when we want the next page but we are already at the last page it should not refresh the pokemon list with the next elements`() = runBlockingTest {
        val firstPage = listOf(
            Pokemon(0, "Zero", "imageUrl", "Fire", null)
        )
        every {
            useCase.invoke(0)
        }.returns(Success(firstPage))
        every {
            useCase.invoke(1)
        }.returns(PageEndOfPages())

        val observe = mockk<Observer<State>>(relaxed = true)

        val viewModel = PokemonListViewModel(useCase)

        viewModel.pokemonListState().observeForever(observe)
        viewModel.fetchPokemons()
        delay(100)
        viewModel.nextPage()

        verifySequence {
            observe.onChanged(State.Loading)
            useCase.invoke(any())
            observe.onChanged(State.Loading)
            observe.onChanged(State.Success(firstPage))
            useCase.invoke(any())
            observe.onChanged(State.LoadingNextPage)
            observe.onChanged(State.Success(firstPage))
        }
        confirmVerified(useCase, observe)
    }

    @Test
    fun `when we observe the pokemonList and the usecase return an error it should return an error state`() {
        every { useCase.invoke(any()) }.returns(
            EmptyError()
        )
        val observe = mockk<Observer<State>>(relaxed = true)

        val viewModel = PokemonListViewModel(useCase)

        viewModel.pokemonListState().observeForever(observe)
        viewModel.fetchPokemons()

        verify { useCase.invoke(any()) }
        verify(atMost = 2) { observe.onChanged(State.Loading) }
        verify { observe.onChanged(State.Error("Empty Pokemon list")) }
        confirmVerified(useCase, observe)
    }
}