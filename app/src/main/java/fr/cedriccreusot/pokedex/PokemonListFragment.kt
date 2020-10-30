package fr.cedriccreusot.pokedex

import PokemonRepositoryAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import fr.cedriccreusot.domain.list.usecase.FetchPokemonListUseCase
import fr.cedriccreusot.pokedex.presentation.list.PokemonListViewModel
import fr.cedriccreusot.pokedex.presentation.list.PokemonListViewModelFactory
import fr.cedriccreusot.pokedex.presentation.list.State
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

class PokemonListFragment : Fragment() {

    private val viewModel: PokemonListViewModel by lazy {
        ViewModelProvider(
            this,
            PokemonListViewModelFactory(
                FetchPokemonListUseCase.create(
                    PokemonRepositoryAdapter(PokeApiClient())
                )
            )
        ).get(
            PokemonListViewModel::class.java
        )
    }

    val adapter =
        PokemonListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_pokemon_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pokedexRecyclerView.adapter = adapter
        viewModel.pokemonList().observe(requireActivity()) {
            when (it) {
                is State.Loading -> {
                    pokedexContainerViewFlipper.displayedChild = 0
                }
                is State.Success -> {
                    pokedexContainerViewFlipper.displayedChild = 1
                    adapter.submitList(it.value)
                }
                is State.Error -> {
                    Snackbar.make(pokedexContainerViewFlipper, it.message, Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
        viewModel.fetchPokemons()

    }
}