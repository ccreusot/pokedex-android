package fr.cedriccreusot.pokedex.presentation.detail

import androidx.lifecycle.Observer
import fr.cedriccreusot.domain.common.model.InvalidArgumentError
import fr.cedriccreusot.domain.common.model.NotFoundError
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.detail.model.PokemonDetail
import fr.cedriccreusot.domain.detail.usecase.FetchPokemonDetailUseCase
import fr.cedriccreusot.pokedex.presentation.detail.PokemonDetailViewModel.State
import fr.cedriccreusot.pokedex.utils.ViewModelTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.junit.Before
import org.junit.Test

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class PokemonDetailViewModelTest : ViewModelTest() {

    private lateinit var useCase: FetchPokemonDetailUseCase
    private lateinit var viewModel: PokemonDetailViewModel

    @Before
    override fun setUp() {
        super.setUp()
        useCase = mockk()
        viewModel = PokemonDetailViewModel(useCase)
    }

    @Test
    fun `when we observe a pokemon details and the usecase returns a NotFoundError - then we should return an error state`() {
        // GIVEN
        every { useCase(90000) } returns NotFoundError(90000)
        val observe = mockk<Observer<State>>(relaxed = true)

        // WHEN
        viewModel.pokemonDetailState().observeForever(observe)
        viewModel.fetchPokemonDetail(90000)

        // THEN
        verify { observe.onChanged(State.Loading) }
        verify { observe.onChanged(State.Error("Pokemon[90000] not found")) }
        confirmVerified(observe)
    }

    @Test
    fun `when we observe a pokemon details and the usecase returns a InvalidArgumentError - then we should return an error state`() {
        // GIVEN
        every { useCase(-1) } returns InvalidArgumentError(-1)
        val observe = mockk<Observer<State>>(relaxed = true)

        // WHEN
        viewModel.pokemonDetailState().observeForever(observe)
        viewModel.fetchPokemonDetail(-1)

        // THEN
        verify { observe.onChanged(State.Loading) }
        verify { observe.onChanged(State.Error("Invalid argument '-1' can not be processed")) }
        confirmVerified(observe)
    }

    @Test
    fun `when we observe a pokemon details and the usecase returns a PokemonDetail - then we should return an success state`() {
        // GIVEN
        val mockedPokemon = mockk<PokemonDetail>(relaxed = true).copy(id = 42)
        every { useCase(42) } returns Success(mockedPokemon)
        val observe = mockk<Observer<State>>(relaxed = true)

        // WHEN
        viewModel.pokemonDetailState().observeForever(observe)
        viewModel.fetchPokemonDetail(42)

        // THEN
        verify { observe.onChanged(State.Loading) }
        verify { observe.onChanged(State.Success(mockedPokemon)) }
        confirmVerified(observe)
    }


}