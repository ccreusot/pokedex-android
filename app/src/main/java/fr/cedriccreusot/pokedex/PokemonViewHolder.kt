package fr.cedriccreusot.pokedex

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import fr.cedriccreusot.domain.list.model.Pokemon
import kotlinx.android.synthetic.main.item_pokemon.view.*

class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(pokemon: Pokemon) = with(itemView) {
        pokemonNameTextView.text = pokemon.name.capitalize()
        pokemonType1TextView.text = pokemon.mainType.capitalize()
        pokemonType2TextView.visibility = View.INVISIBLE
        pokemon.secondaryType?.let {
            pokemonType2TextView.text = it.capitalize()
            pokemonType2TextView.visibility = View.VISIBLE
        }
        pokemonIndexTextView.text = "#%03d".format(pokemon.id)
        pokemonImageView.load(pokemon.imageUrl)

        this.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, pokemon.getTypeResColor()))

    }
}
