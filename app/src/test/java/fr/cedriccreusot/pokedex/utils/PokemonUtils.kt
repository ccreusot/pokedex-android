package fr.cedriccreusot.pokedex.utils

import me.sargunvohra.lib.pokekotlin.model.NamedApiResource
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import me.sargunvohra.lib.pokekotlin.model.PokemonSprites
import me.sargunvohra.lib.pokekotlin.model.PokemonType

internal fun mockedPokemon() = Pokemon(
    0,
    "nyanCat",
    0,
    0,
    true,
    0,
    0,
    NamedApiResource("", "", 0),
    emptyList(),
    emptyList(),
    emptyList(),
    emptyList(),
    emptyList(),
    emptyList(),
    listOf(PokemonType(0, NamedApiResource("Fire", "Cat", 0))),
    PokemonSprites(null, null, null, null, null, null, null, null)
)
