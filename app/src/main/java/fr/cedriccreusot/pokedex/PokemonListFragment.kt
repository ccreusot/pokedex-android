package fr.cedriccreusot.pokedex

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import fr.cedriccreusot.pokedex.presentation.list.PokemonListViewModel
import fr.cedriccreusot.pokedex.presentation.list.State
import fr.cedriccreusot.pokedex.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_pokemon_list.*

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    private val viewModel: PokemonListViewModel by viewModels()

    private val pokemonAdapter = PokemonListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_pokemon_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pokedexRecyclerView.adapter = pokemonAdapter
        pokedexRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    2f,
                    resources.displayMetrics
                ).toInt(),
                true
            )
        )
        viewModel.pokemonList().observe(requireActivity()) {
            when (it) {
                is State.Loading -> {
                    pokedexContainerViewFlipper.displayedChild = 0
                }
                is State.Success -> {
                    pokedexContainerViewFlipper.displayedChild = 1
                    pokemonAdapter.submitList(it.value)
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