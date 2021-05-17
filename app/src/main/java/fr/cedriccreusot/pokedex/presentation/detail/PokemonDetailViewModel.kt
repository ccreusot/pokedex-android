package fr.cedriccreusot.pokedex.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.cedriccreusot.domain.common.model.Error
import fr.cedriccreusot.domain.common.model.Success
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
                    is Success -> State.Success(result.value)
                    else -> State.Error((result as Error).message)
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
