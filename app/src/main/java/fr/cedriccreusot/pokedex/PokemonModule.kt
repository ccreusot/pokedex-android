package fr.cedriccreusot.pokedex

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import fr.cedriccreusot.domain.list.repository.PokemonRepository
import fr.cedriccreusot.domain.list.usecase.FetchPokemonListUseCase
import fr.cedriccreusot.pokedex.dataadapter.PokemonRepositoryAdapter
import me.sargunvohra.lib.pokekotlin.client.PokeApi
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

@Module
@InstallIn(ActivityRetainedComponent::class)
class PokemonModule {
    @Provides
    fun providePokeApi(): PokeApi = PokeApiClient()

    @Provides
    fun provideFetchPokemonListUseCase(repository: PokemonRepository): FetchPokemonListUseCase =
        FetchPokemonListUseCase.create(repository)
}

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class PokemonRepositoryModule {
    @Binds
    abstract fun bindPokemonRepository(pokemonRepositoryAdapter: PokemonRepositoryAdapter): PokemonRepository
}