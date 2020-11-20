package fr.cedriccreusot.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import fr.cedriccreusot.domain.list.model.Pokemon
import kotlinx.android.synthetic.main.item_pokemon.view.*

class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(pokemon: Pokemon) = with(itemView) {
        pokemonNameTextView.text = pokemon.name
    }
}