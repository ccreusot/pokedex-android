package fr.cedriccreusot.domain.detail.model

import fr.cedriccreusot.domain.list.model.Pokemon

typealias Male = String
typealias Female = String
typealias Level = Int

data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val mainType: String,
    val secondaryType: String?,
    val description: String,
    val species: String,
    val height: String,
    val weight: String,
    val abilities: List<String>,
    val genderParity: Pair<Male, Female>,
    val eggGroups: List<String>,
    val eggCycle: String,
    val stats: PokemonStats,
    val pokemonEvolutions: Map<Level, Pokemon>,
    val moves: List<Move>
)

data class PokemonStats(
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val spAttack: Int,
    val spDefense: Int,
    val speed: Int,
)

data class Move(
    val name: String,
    val pp: Int,
    val power: Int,
    val priority: Int,
    val type: String,
    val accuracy: Int,
    val category: String,
    val description: String,
)