package fr.cedriccreusot.domain.detail.usecase

import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.common.repository.PokemonRepository
import fr.cedriccreusot.domain.detail.model.PokemonDetail

interface FetchPokemonDetailUseCase {
    operator fun invoke(pokemonId: Int): Result<PokemonDetail>

    companion object {
        fun create(repository: PokemonRepository): FetchPokemonDetailUseCase =
            FetchPokemonDetailUseCaseImpl(repository)
    }
}

internal class FetchPokemonDetailUseCaseImpl(
    private val pokemonRepository: PokemonRepository
) : FetchPokemonDetailUseCase {
    override fun invoke(pokemonId: Int): Result<PokemonDetail> {
        return pokemonRepository.getPokemon(pokemonId)
    }
}
