package fr.cedriccreusot.pokedex.presentation.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.cedriccreusot.domain.common.model.Error
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.domain.list.usecase.FetchPokemonListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class State {
    object Loading : State()
    data class Success(val value: List<Pokemon>) : State()
    data class Error(val message: String) : State()
}

class PokemonListViewModel @ViewModelInject constructor(private val useCase: FetchPokemonListUseCase) :
    ViewModel() {

    private val pokemonList: MutableLiveData<State> = MutableLiveData<State>(State.Loading)

    fun pokemonList(): LiveData<State> = pokemonList

    fun fetchPokemons() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = useCase(0)
            pokemonList.postValue(State.Loading)
            when (result) {
                is Success -> {
                    pokemonList.postValue(State.Success(result.value))
                }
                is Error -> {
                    pokemonList.postValue(State.Error(result.message))
                }
            }
        }
    }
}