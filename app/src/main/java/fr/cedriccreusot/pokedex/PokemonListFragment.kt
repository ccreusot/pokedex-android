package fr.cedriccreusot.pokedex

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import fr.cedriccreusot.pokedex.databinding.FragmentPokemonListBinding
import fr.cedriccreusot.pokedex.presentation.list.PokemonListViewModel
import fr.cedriccreusot.pokedex.presentation.list.State
import fr.cedriccreusot.pokedex.utils.GridSpacingItemDecoration
import java.lang.ref.WeakReference

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    private val viewModel: PokemonListViewModel by viewModels()

    private val pokemonAdapter = PokemonListAdapter()

    private lateinit var binding: WeakReference<FragmentPokemonListBinding>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WeakReference(FragmentPokemonListBinding.inflate(inflater))
        return binding.get()?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.get()?.run {
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
            pokedexRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    (recyclerView.layoutManager as? GridLayoutManager)?.let {
                        if (it.findLastVisibleItemPosition() - it.itemCount <= 1) {
                            viewModel.nextPage()
                        }
                    }
                }
            })
            viewModel.pokemonListState().observe(requireActivity()) {
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
                    is State.LoadingNextPage -> {
                        Snackbar.make(pokedexContainerViewFlipper, "Loading next page...", Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
        viewModel.fetchPokemons()
    }
}