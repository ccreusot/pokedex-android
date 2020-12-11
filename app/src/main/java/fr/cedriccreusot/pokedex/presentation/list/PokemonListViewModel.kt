package fr.cedriccreusot.pokedex.presentation.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.cedriccreusot.domain.common.model.Error
import fr.cedriccreusot.domain.common.model.PageEndOfPages
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.domain.list.usecase.FetchPokemonListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class State {
    object Loading : State()
    object LoadingNextPage : State()
    data class Success(val value: List<Pokemon>) : State()
    data class Error(val message: String) : State()
}

class PokemonListViewModel @ViewModelInject constructor(private val useCase: FetchPokemonListUseCase) :
    ViewModel() {

    private val pokemonListState: MutableLiveData<State> = MutableLiveData<State>(State.Loading)
    private var pokemonList: List<Pokemon> = emptyList()

    fun pokemonListState(): LiveData<State> = pokemonListState

    fun fetchPokemons() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = useCase(0)
            pokemonListState.postValue(State.Loading)
            when (result) {
                is Success -> {
                    pokemonList = result.value
                    pokemonListState.postValue(State.Success(result.value))
                }
                is Error -> {
                    pokemonListState.postValue(State.Error(result.message))
                }
            }
        }
    }

    fun nextPage() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = useCase(1)
            pokemonListState.postValue(State.LoadingNextPage)
            when (result) {
                is Success -> {
                    pokemonList = pokemonList + result.value
                    pokemonListState.postValue(State.Success(pokemonList))
                }
                is PageEndOfPages -> {
                    pokemonListState.postValue(State.Success(pokemonList))
                }
            }
        }
    }
}