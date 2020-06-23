package fr.cedriccreusot.pokedex.presentation.list

import androidx.lifecycle.*
import fr.cedriccreusot.domain.common.model.Error
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.domain.list.usecase.FetchPokemonListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class State {
    object Loading : State()
    data class Success(val value: List<Pokemon>) : State()
    data class Error(val message: String) : State()
}

class PokemonListViewModelFactory(val useCase: fr.cedriccreusot.domain.list.usecase.FetchPokemonListUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(fr.cedriccreusot.domain.list.usecase.FetchPokemonListUseCase::class.java)
            .newInstance(useCase)
    }
}

class PokemonListViewModel(private val useCase: fr.cedriccreusot.domain.list.usecase.FetchPokemonListUseCase) :
    ViewModel() {

    private val pokemonList: MutableLiveData<State> = MutableLiveData<State>(State.Loading)

    fun pokemonList(): LiveData<State> = pokemonList

    fun fetchPokemons() {
        viewModelScope.launch(Dispatchers.IO) {
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