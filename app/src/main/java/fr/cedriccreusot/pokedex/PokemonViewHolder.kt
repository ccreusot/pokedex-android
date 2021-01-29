package fr.cedriccreusot.pokedex

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.pokedex.databinding.ItemLoaderBinding
import fr.cedriccreusot.pokedex.databinding.ItemPokemonBinding

class PokemonViewHolder(private val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(pokemon: Pokemon) = with(binding) {
        pokemonNameTextView.text = pokemon.name.capitalize()
        pokemonType1TextView.text = pokemon.mainType.capitalize()
        pokemonType2TextView.visibility = View.INVISIBLE
        pokemon.secondaryType?.let {
            pokemonType2TextView.text = it.capitalize()
            pokemonType2TextView.visibility = View.VISIBLE
        }
        pokemonIndexTextView.text = "#%03d".format(pokemon.id)
        pokemonImageView.load(pokemon.imageUrl)
        root.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, pokemon.getTypeResColor()))
    }
}

class PokemonLoadMoreViewHolder(private val binding: ItemLoaderBinding) : RecyclerView.ViewHolder(binding.root)