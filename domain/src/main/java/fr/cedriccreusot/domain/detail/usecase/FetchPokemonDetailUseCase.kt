package fr.cedriccreusot.domain.detail.usecase

import fr.cedriccreusot.domain.common.model.InvalidArgumentError
import fr.cedriccreusot.domain.common.model.Result
import fr.cedriccreusot.domain.common.repository.PokemonDetailRepository
import fr.cedriccreusot.domain.detail.model.PokemonDetail

interface FetchPokemonDetailUseCase {
    operator fun invoke(pokemonId: Int): Result<PokemonDetail>

    companion object {
        fun create(repository: PokemonDetailRepository): FetchPokemonDetailUseCase =
            FetchPokemonDetailUseCaseImpl(repository)
    }
}

internal class FetchPokemonDetailUseCaseImpl(
    private val repository: PokemonDetailRepository
) : FetchPokemonDetailUseCase {
    override fun invoke(pokemonId: Int): Result<PokemonDetail> =
        if (pokemonId < 0) {
            InvalidArgumentError(pokemonId)
        } else {
            repository.getPokemon(pokemonId)
        }
}
