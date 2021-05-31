package fr.cedriccreusot.pokedex

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import fr.cedriccreusot.domain.common.repository.PokemonDetailRepository
import fr.cedriccreusot.domain.common.repository.PokemonListRepository
import fr.cedriccreusot.domain.detail.usecase.FetchPokemonDetailUseCase
import fr.cedriccreusot.domain.list.usecase.FetchPokemonListUseCase
import fr.cedriccreusot.pokedex.dataadapter.PokemonDetailRepositoryAdapter
import fr.cedriccreusot.pokedex.dataadapter.PokemonListRepositoryAdapter
import me.sargunvohra.lib.pokekotlin.client.PokeApi
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

@Module
@InstallIn(ActivityRetainedComponent::class)
class PokemonModule {
    @Provides
    fun providePokeApi(): PokeApi = PokeApiClient()

    @Provides
    fun provideFetchPokemonListUseCase(repository: PokemonListRepository): FetchPokemonListUseCase =
        FetchPokemonListUseCase.create(repository)

    @Provides
    fun provideFetchPokemonDetailUseCase(repository: PokemonDetailRepository): FetchPokemonDetailUseCase =
        FetchPokemonDetailUseCase.create(repository)
}

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class PokemonRepositoryModule {
    @Binds
    abstract fun bindPokemonListRepository(pokemonListRepositoryAdapter: PokemonListRepositoryAdapter): PokemonListRepository

    @Binds
    abstract fun bindPokemonDetailRepository(pokemonDetailRepositoryAdapter: PokemonDetailRepositoryAdapter): PokemonDetailRepository
}