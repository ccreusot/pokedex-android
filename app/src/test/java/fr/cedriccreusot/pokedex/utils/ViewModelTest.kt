package fr.cedriccreusot.pokedex.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
abstract class ViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    open fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    open fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

}