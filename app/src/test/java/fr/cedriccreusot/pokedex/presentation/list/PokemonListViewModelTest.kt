package fr.cedriccreusot.pokedex.presentation.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import fr.cedriccreusot.domain.common.model.EmptyError
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.domain.list.usecase.FetchPokemonListUseCase
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PokemonListViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `when we observe the pokemonList it should start loading then return the list of pokemons`() {
        val useCase = mockk<FetchPokemonListUseCase>()

        val pokemonList = listOf(
            Pokemon(0, "Zero", "imageUrl", "Fire", null)
        )
        every { useCase.invoke() }.returns(
            Success(
                pokemonList
            )
        )

        val observe = mockk<Observer<State>>(relaxed = true)

        val viewModel = PokemonListViewModel(useCase)

        viewModel.pokemonList().observeForever(observe)
        viewModel.fetchPokemons()

        verify { useCase.invoke() }
        verify(atMost = 2) { observe.onChanged(State.Loading) }
        verify { observe.onChanged(State.Success(pokemonList)) }
        confirmVerified(useCase, observe)
    }

    @Test
    fun `when we observe the pokemonList and the usecase return an error it should return an error state`() {
        val useCase = mockk<FetchPokemonListUseCase>()

        every { useCase.invoke() }.returns(
            EmptyError()
        )

        val observe = mockk<Observer<State>>(relaxed = true)

        val viewModel = PokemonListViewModel(useCase)

        viewModel.pokemonList().observeForever(observe)
        viewModel.fetchPokemons()

        verify { useCase.invoke() }
        verify(atMost = 2) { observe.onChanged(State.Loading) }
        verify { observe.onChanged(State.Error("Empty Pokemon list")) }
        confirmVerified(useCase, observe)
    }
}