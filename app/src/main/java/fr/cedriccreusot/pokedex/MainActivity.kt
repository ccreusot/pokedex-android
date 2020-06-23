package fr.cedriccreusot.pokedex

import PokemonRepositoryAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import fr.cedriccreusot.domain.list.model.Pokemon
import fr.cedriccreusot.domain.list.usecase.FetchPokemonListUseCase
import fr.cedriccreusot.pokedex.presentation.list.PokemonListViewModel
import fr.cedriccreusot.pokedex.presentation.list.PokemonListViewModelFactory
import fr.cedriccreusot.pokedex.presentation.list.State
import kotlinx.android.synthetic.main.activity_main.*
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

class MainActivity : AppCompatActivity() {

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
        object : ListAdapter<Pokemon, PokemonViewHolder>(object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem == newItem
        }) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
                return LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false).let {
                        PokemonViewHolder(it)
                    }
            }

            override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
                holder.bind(getItem(position))
            }
        }


    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pokemon: Pokemon) {
            itemView.findViewById<TextView>(android.R.id.text1).text = pokemon.name
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pokedexRecyclerView.adapter = adapter
        viewModel.pokemonList().observe(this, Observer {
            when(it) {
                is State.Loading -> {
                    pokedexContainerViewFlipper.displayedChild = 0
                }
                is State.Success -> {
                    pokedexContainerViewFlipper.displayedChild = 1
                    adapter.submitList(it.value)
                }
                is State.Error -> {
                    Snackbar.make(pokedexContainerViewFlipper, it.message, Snackbar.LENGTH_LONG).show()
                }
            }
        })
        viewModel.fetchPokemons()

    }


}