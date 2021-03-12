package fr.cedriccreusot.pokedex

import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import fr.cedriccreusot.pokedex.databinding.FragmentPokemonDetailBinding
import java.lang.ref.WeakReference

class PokemonDetailFragment : Fragment() {

    private val args: PokemonDetailFragmentArgs by navArgs()
    private lateinit var binding: WeakReference<FragmentPokemonDetailBinding>

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
            ViewCompat.setTransitionName(pokeballImageView, "pokeball_${args.pokemonId}")
        }
        return binding.get()?.root
    }
}