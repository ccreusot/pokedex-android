package fr.cedriccreusot.pokedex.presentation.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import fr.cedriccreusot.pokedex.domain.common.model.Success
import fr.cedriccreusot.pokedex.domain.list.model.Pokemon
import fr.cedriccreusot.pokedex.domain.list.usecase.FetchPokemonListUseCase
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
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
    fun `when we observe the pokemonList it should start loading then return the list of pokemons`() =
        runBlocking {
            val useCase = mockk<FetchPokemonListUseCase>()

            val pokemonList = listOf(
                Pokemon(0, "Zero")
            )
            every { useCase.invoke() }.returns(
                Success(
                    pokemonList
                )
            )

            val observe = mockk<Observer<State>>(relaxed = true)

            val viewModel = PokemonListViewModel(useCase)

            viewModel.pokemonList.observeForever(observe)

            verify { useCase.invoke() }
            verify(atMost = 2) { observe.onChanged(State.Loading) }
            verify { observe.onChanged(State.Success(pokemonList)) }
            confirmVerified(useCase, observe)
        }
}