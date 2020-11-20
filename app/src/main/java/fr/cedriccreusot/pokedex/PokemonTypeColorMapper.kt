package fr.cedriccreusot.pokedex

import androidx.annotation.ColorRes
import fr.cedriccreusot.domain.list.model.Pokemon
import java.util.*

@ColorRes
fun Pokemon.getTypeResColor(): Int = when (mainType.toLowerCase(Locale.getDefault())) {
    "grass", "bug" -> R.color.pokeLightTeal
    "fire" -> R.color.pokeLightRed
    "water", "fighting", "normal" -> R.color.pokeLightBlue
    "electric", "psychic" -> R.color.pokeLightYellow
    "poison", "ghost" -> R.color.pokeLightPurple
    "ground", "rock" -> R.color.pokeLightBrown
    "dark" -> R.color.pokeBlack
    else -> R.color.pokeLightBlue
}
