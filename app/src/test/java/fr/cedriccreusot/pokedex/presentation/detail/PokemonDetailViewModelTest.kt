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
    private lateinit var observer: Observer<State>

    companion object {
        private const val NOT_FOUND_ERROR_ARG = 90000
        private const val NOT_FOUND_ERROR_MESSAGE = "Pokemon[90000] not found"
        private const val INVALID_ARGUMENT_ERROR_ARG = -1
        private const val INVALID_ARGUMENT_ERROR_MESSAGE =
            "Invalid argument '-1' can not be processed"
        private const val SUCCESSFUL_ARG = 42
    }

    @Before
    override fun setUp() {
        super.setUp()
        useCase = mockk()
        viewModel = PokemonDetailViewModel(useCase)
        observer = mockk(relaxed = true)
    }

    @Test
    fun `when we observe a pokemon details and the usecase returns a NotFoundError - then we should return an error state`() {
        // GIVEN
        every { useCase(NOT_FOUND_ERROR_ARG) } returns NotFoundError(NOT_FOUND_ERROR_ARG)

        // WHEN
        viewModel.pokemonDetailState().observeForever(observer)
        viewModel.fetchPokemonDetail(NOT_FOUND_ERROR_ARG)

        // THEN
        verify { observer.onChanged(State.Loading) }
        verify { observer.onChanged(State.Error(NOT_FOUND_ERROR_MESSAGE)) }
        confirmVerified(observer)
    }

    @Test
    fun `when we observe a pokemon details and the usecase returns a InvalidArgumentError - then we should return an error state`() {
        // GIVEN
        every { useCase(INVALID_ARGUMENT_ERROR_ARG) } returns InvalidArgumentError(
            INVALID_ARGUMENT_ERROR_ARG
        )

        // WHEN
        viewModel.pokemonDetailState().observeForever(observer)
        viewModel.fetchPokemonDetail(INVALID_ARGUMENT_ERROR_ARG)

        // THEN
        verify { observer.onChanged(State.Loading) }
        verify { observer.onChanged(State.Error(INVALID_ARGUMENT_ERROR_MESSAGE)) }
        confirmVerified(observer)
    }

    @Test
    fun `when we observe a pokemon details and the usecase returns a PokemonDetail - then we should return an success state`() {
        // GIVEN
        val mockedPokemon = mockk<PokemonDetail>(relaxed = true).copy(id = SUCCESSFUL_ARG)
        every { useCase(SUCCESSFUL_ARG) } returns Success(mockedPokemon)

        // WHEN
        viewModel.pokemonDetailState().observeForever(observer)
        viewModel.fetchPokemonDetail(SUCCESSFUL_ARG)

        // THEN
        verify { observer.onChanged(State.Loading) }
        verify { observer.onChanged(State.Success(mockedPokemon)) }
        confirmVerified(observer)
    }


}