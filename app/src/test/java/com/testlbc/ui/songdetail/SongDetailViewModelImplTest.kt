package com.testlbc.ui.songdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.testlbc.core.EventPath
import com.testlbc.data.interactor.GetSongInteractor
import com.testlbc.data.interactor.GetSongInteractor.Result
import com.testlbc.data.repository.local.Song
import com.testlbc.ui.songdetail.SongDetailViewModel.Path
import com.testlbc.ui.songdetail.SongDetailViewModel.State
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SongDetailViewModelImplTest : KoinTest {

    @Rule
    @JvmField
    val instantTaskRule = InstantTaskExecutorRule()

    @Mock
    lateinit var getSongInteractor: GetSongInteractor

    @Mock
    lateinit var song: Song

    @Mock
    lateinit var observer: Observer<State>

    private lateinit var subject: SongDetailViewModelImpl

    private val state: MediatorLiveData<State> = MediatorLiveData()
    private val navigation: MutableLiveData<EventPath<Path>> = MutableLiveData()
    private val getSongInteractorLiveData: MutableLiveData<Result> =
        MutableLiveData()

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
        state.observeForever(observer)
        given(getSongInteractor.getLiveData()).willReturn(getSongInteractorLiveData)
    }

    @Test
    fun `fetchSong - execute`() {
        `when song is fetched`()
        `then interactor should be executed`()
        `then interactor should have no more interactions`()
    }

    @Test
    fun `fetchSong - success`() {
        `when fetched song has result`(Result.OnSuccess(song))
        `then state observer should receive state`(State.SongLoaded(song))
    }

    @Test
    fun `fetchSong - error`() {
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
        then(getSongInteractor).should().execute(SONG_ID)
    }

    private fun `then interactor should have no more interactions`() {
        then(getSongInteractor).should().getLiveData()
        then(getSongInteractor).shouldHaveNoMoreInteractions()
    }

    private fun `then state observer should receive state`(state: State) {
        then(observer).should().onChanged(state)
        then(observer).shouldHaveNoMoreInteractions()
    }

    companion object {
        private const val SONG_ID = 0
    }
}
