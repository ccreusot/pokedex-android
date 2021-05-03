package fr.cedriccreusot.pokedex.presentation.detail

import androidx.lifecycle.Observer
import fr.cedriccreusot.domain.common.model.NotFoundError
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
        verify { observe.onChanged(State.Error("Pokemon not found")) }
        confirmVerified(observe)
    }
}