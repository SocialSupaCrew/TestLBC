package com.testlbc.ui.albumdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.testlbc.core.EventPath
import com.testlbc.data.interactor.GetAlbumInteractor
import com.testlbc.data.interactor.GetAlbumInteractor.Result
import com.testlbc.data.repository.local.Song
import com.testlbc.rules.MainCoroutineRule
import com.testlbc.ui.albumdetail.AlbumDetailViewModel.Path
import com.testlbc.ui.albumdetail.AlbumDetailViewModel.State
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
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get

class AlbumDetailViewModelImplTest : KoinTest {

    @Rule
    @JvmField
    val instantTaskRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val getAlbumInteractor: GetAlbumInteractor = mockk()
    private val songs: List<Song> = listOf(Song(1, 1, "", "", ""))

    private lateinit var subject: AlbumDetailViewModelImpl

    private val state: MediatorLiveData<State> = MediatorLiveData()
    private val navigation: MutableLiveData<EventPath<Path>> = MutableLiveData()
    private val getSongInteractorLiveData: MutableLiveData<Result> = MutableLiveData()

    private val albumDetailTestModule: Module = module {
        viewModel { (albumId: Int) ->
            AlbumDetailViewModelImpl(state, navigation, getAlbumInteractor, albumId)
        }
    }


    @Before
    fun setUp() {
        startKoin { modules(albumDetailTestModule) }
        setUpLiveData()
        subject = get { parametersOf(ALBUM_ID) }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    private fun setUpLiveData() {
        every { getAlbumInteractor.getLiveData() } returns getSongInteractorLiveData
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetchSongs - execute`() = runTest {
        `when songs are fetched`()
        `then interactor should be executed`()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetchSongs - success`() = runTest {
        `when fetched songs has result`(Result.OnSuccess(songs))
        `then state observer should receive state`(State.SongsLoaded(songs))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetchSongs - error`() = runTest {
        `when fetched songs has result`(Result.OnError)
        `then state observer should receive state`(State.ShowError)
    }

    private fun `when songs are fetched`() {
        subject.fetchSongs()
    }

    private fun `when fetched songs has result`(result: Result) {
        getSongInteractorLiveData.value = result
    }

    private fun `then interactor should be executed`() {
        coVerify { getAlbumInteractor.execute(ALBUM_ID) }
    }

    private fun `then state observer should receive state`(state: State) {
        assertLiveData(subject.getState()).isEqualTo(state)
    }

    companion object {
        private const val ALBUM_ID = 0
    }
}
