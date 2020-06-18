package fr.cedriccreusot.pokedex.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.cedriccreusot.pokedex.domain.common.model.Error
import fr.cedriccreusot.pokedex.domain.common.model.Success
import fr.cedriccreusot.pokedex.domain.list.model.Pokemon
import fr.cedriccreusot.pokedex.domain.list.usecase.FetchPokemonListUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class State {
    object Loading : State()
    data class Success(val value: List<Pokemon>) : State()
    data class Error(val message: String) : State()
}

fun <T> LiveData<T>.postValue(value: T) {
    (this as MutableLiveData).postValue(value)
}

class PokemonListViewModel(private val useCase: FetchPokemonListUseCase) : ViewModel() {

    val pokemonList: LiveData<State> by lazy {
        fetchPokemons()
        MutableLiveData<State>(State.Loading)
    }

    fun fetchPokemons() {
        viewModelScope.launch {
            val result = useCase()
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