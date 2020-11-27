package fr.cedriccreusot.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.pokedex.databinding.ItemPokemonBinding

class PokemonListAdapter : ListAdapter<Pokemon, PokemonViewHolder>(object : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
        oldItem === newItem

    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
        oldItem == newItem
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return ItemPokemonBinding.inflate(LayoutInflater.from(parent.context)).let {
                PokemonViewHolder(it)
            }
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}