package com.testlbc.ui.songdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.testlbc.core.EventPath
import com.testlbc.data.interactor.GetSongInteractor
import com.testlbc.data.interactor.GetSongInteractor.Result
import com.testlbc.data.repository.local.Song
import com.testlbc.rules.MainCoroutineRule
import com.testlbc.ui.songdetail.SongDetailViewModel.Path
import com.testlbc.ui.songdetail.SongDetailViewModel.State
import com.testlbc.utils.assertLiveData
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get

class SongDetailViewModelImplTest : KoinTest {

    @Rule
    @JvmField
    val instantTaskRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val getSongInteractor: GetSongInteractor = mockk()
    private val song: Song = Song(1, 1, "", "", "")

    private lateinit var subject: SongDetailViewModelImpl

    private val state: MediatorLiveData<State> = MediatorLiveData()
    private val navigation: MutableLiveData<EventPath<Path>> = MutableLiveData()
    private val getSongInteractorLiveData: MutableLiveData<Result> = MutableLiveData()

    private val songDetailTestModule: Module = module {
        single { (songId: Int) ->
            SongDetailViewModelImpl(state, navigation, getSongInteractor, songId)
        }
    }

    @Before
    fun setUp() {
        startKoin { modules(songDetailTestModule) }
        setUpLiveData()
        subject = get { parametersOf(SONG_ID) }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    private fun setUpLiveData() {
        every { getSongInteractor.getLiveData() } returns getSongInteractorLiveData
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetchSong - execute`() = runTest {
        `when song is fetched`()
        `then interactor should be executed`()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetchSong - success`() = runTest {
        `when fetched song has result`(Result.OnSuccess(song))
        `then state observer should receive state`(State.SongLoaded(song))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetchSong - error`() = runTest {
        `when fetched song has result`(Result.OnError)
        `then state observer should receive state`(State.ShowError)
    }

    private fun `when song is fetched`() {
        subject.fetchSong()
    }

    private fun `when fetched song has result`(result: Result) {
        getSongInteractorLiveData.value = result
    }

    private fun `then interactor should be executed`() {
        coVerify { getSongInteractor.execute(SONG_ID) }
    }

    private fun `then state observer should receive state`(state: State) {
        assertLiveData(subject.getState()).isEqualTo(state)
    }

    companion object {
        private const val SONG_ID = 0
    }
}
