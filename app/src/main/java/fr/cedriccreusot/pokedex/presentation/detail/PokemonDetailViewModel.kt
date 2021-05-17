package fr.cedriccreusot.pokedex.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.cedriccreusot.domain.common.model.InvalidArgumentError
import fr.cedriccreusot.domain.common.model.NotFoundError
import fr.cedriccreusot.domain.detail.model.PokemonDetail
import fr.cedriccreusot.domain.detail.usecase.FetchPokemonDetailUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonDetailViewModel constructor(
    private val useCase: FetchPokemonDetailUseCase
) : ViewModel() {

    private val _pokemonDetailState: MutableLiveData<State> = MutableLiveData()

    fun pokemonDetailState(): LiveData<State> = _pokemonDetailState

    fun fetchPokemonDetail(pokemonId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonDetailState.postValue(State.Loading)
            _pokemonDetailState.postValue(
                when (val result = useCase(pokemonId)) {
                    is InvalidArgumentError -> State.Error(result.message)
                    is NotFoundError -> State.Error(result.message)
                    else -> TODO()
                }
            )
        }
    }

    sealed class State {
        object Loading : State()
        data class Success(val value: PokemonDetail) : State()
        data class Error(val message: String) : State()
    }

}
