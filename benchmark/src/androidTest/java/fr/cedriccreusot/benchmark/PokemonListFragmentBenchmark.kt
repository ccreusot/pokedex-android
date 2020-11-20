package fr.cedriccreusot.benchmark

import androidx.benchmark.junit4.BenchmarkRule
//import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.annotation.UiThreadTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import fr.cedriccreusot.presentation.MainActivity
import fr.cedriccreusot.presentation.PokemonListFragment
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class PokemonListFragmentBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @UiThreadTest
    @Test
    fun testPokemonListFragment() {
//        launchFragmentInContainer<PokemonListFragment>()
    }
}