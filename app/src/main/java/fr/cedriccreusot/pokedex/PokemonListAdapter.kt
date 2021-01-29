package fr.cedriccreusot.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.pokedex.databinding.ItemLoaderBinding
import fr.cedriccreusot.pokedex.databinding.ItemPokemonBinding

class PokemonListAdapter : ListAdapter<Pokemon, RecyclerView.ViewHolder>(PokemonDiffCallback) {
    private var isLoadingMore: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            POKEMON_CARD_VIEW_TYPE -> PokemonViewHolder(
                ItemPokemonBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    )
                )
            )
            else -> PokemonLoadMoreViewHolder(ItemLoaderBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PokemonViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getItemCount(): Int {
        return currentList.size + if (isLoadingMore) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoadingMore && position >= currentList.size) {
            POKEMON_LOAD_MORE_VIEW_TYPE
        } else {
            POKEMON_CARD_VIEW_TYPE
        }
    }

    fun showLoadMore() {
        isLoadingMore = true
        notifyDataSetChanged()
    }

    private fun hideLoadMore() {
        isLoadingMore = false
        notifyDataSetChanged()
    }

    override fun submitList(list: List<Pokemon>?) {
        hideLoadMore()
        super.submitList(list)
    }

    override fun submitList(list: List<Pokemon>?, commitCallback: Runnable?) {
        hideLoadMore()
        super.submitList(list, commitCallback)
    }

    companion object {
        const val POKEMON_CARD_VIEW_TYPE = 0
        const val POKEMON_LOAD_MORE_VIEW_TYPE = 1
    }
}


object PokemonDiffCallback : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
        oldItem === newItem

    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
        oldItem == newItem
}