package fr.cedriccreusot.domain.list.model

data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val mainType: String,
    val secondaryType: String?,
)
