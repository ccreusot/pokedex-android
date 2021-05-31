package fr.cedriccreusot.pokedex

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import fr.cedriccreusot.pokedex.databinding.FragmentPokemonDetailBinding
import fr.cedriccreusot.pokedex.presentation.detail.PokemonDetailViewModel
import java.lang.ref.WeakReference

class PokemonDetailFragment : Fragment() {

    private val args: PokemonDetailFragmentArgs by navArgs()
    private lateinit var binding: WeakReference<FragmentPokemonDetailBinding>

    private val viewModel: PokemonDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WeakReference(FragmentPokemonDetailBinding.inflate(inflater))
        binding.get()?.run {
            pokemonImageView.load(args.pokemonImg)
            ViewCompat.setTransitionName(pokeballImageView, "pokeball_${args.pokemonId}")
            ViewCompat.setTransitionName(pokemonImageView, "pokemon_${args.pokemonId}")
        }
        TODO("LOAD THE POKEMON DETAIL !")
        return binding.get()?.root
    }
}
