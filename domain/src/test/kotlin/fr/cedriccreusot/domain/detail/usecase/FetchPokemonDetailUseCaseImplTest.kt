package fr.cedriccreusot.domain.detail.usecase

import com.google.common.truth.Truth.assertThat
import fr.cedriccreusot.domain.common.model.InvalidArgumentError
import fr.cedriccreusot.domain.common.model.NotFoundError
import fr.cedriccreusot.domain.common.model.Success
import fr.cedriccreusot.domain.common.repository.PokemonRepository
import fr.cedriccreusot.domain.detail.model.PokemonDetail
import fr.cedriccreusot.domain.detail.model.PokemonStats
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class FetchPokemonDetailUseCaseImplTest {

    private val repository: PokemonRepository = mockk()
    private val useCase: FetchPokemonDetailUseCase = FetchPokemonDetailUseCaseImpl(repository)

    @Test
    fun `when we fetch a pokemon by id we should have a pokemon details object`() {

        // GIVEN

        every { repository.getPokemon(12) } returns Success(
            PokemonDetail(
                12,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                emptyList(),
                "" to "",
                emptyList(),
                "",
                PokemonStats(1, 1, 1, 1, 1, 1),
                emptyMap(),
                emptyList()
            )
        )

        // WHEN

        val result = useCase(12)

        // THEN

        verify { repository.getPokemon(12) }
        assertThat(result).isInstanceOf(Success::class.java)
        assertThat((result as Success<PokemonDetail>).value).isInstanceOf(PokemonDetail::class.java)
    }

    @Test
    fun `when we fetch a pokemon by id and it is not found should return a NotFoundError`() {

        // GIVEN

        every { repository.getPokemon(9001) } returns NotFoundError("kamoulox")

        // WHEN

        val result = useCase(9001)

        // THEN

        verify { repository.getPokemon(9001) }
        assertThat(result).isInstanceOf(NotFoundError::class.java)
    }

    @Test
    fun `when we fetch a pokemon by id and the latter is negative should return an InvalidArgumentError`() {

        // WHEN

        val result = useCase(-1)

        // THEN

        assertThat(result).isInstanceOf(InvalidArgumentError::class.java)
    }
}
