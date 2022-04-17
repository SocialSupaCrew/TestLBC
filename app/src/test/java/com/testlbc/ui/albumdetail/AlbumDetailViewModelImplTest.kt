package com.testlbc.ui.albumdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.testlbc.core.EventPath
import com.testlbc.data.interactor.GetAlbumInteractor
import com.testlbc.data.interactor.GetAlbumInteractor.Result
import com.testlbc.data.repository.local.Song
import com.testlbc.ui.albumdetail.AlbumDetailViewModel.Path
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AlbumDetailViewModelImplTest : KoinTest {

    @Rule
    @JvmField
    val instantTaskRule = InstantTaskExecutorRule()

    @Mock
    lateinit var getAlbumInteractor: GetAlbumInteractor

    @Mock
    lateinit var songs: List<Song>

    @Mock
    lateinit var observer: Observer<AlbumDetailViewModel.State>

    private lateinit var subject: AlbumDetailViewModelImpl

    private val state: MediatorLiveData<AlbumDetailViewModel.State> = MediatorLiveData()
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
        state.observeForever(observer)
        BDDMockito.given(getAlbumInteractor.getLiveData()).willReturn(getSongInteractorLiveData)
    }

    @Test
    fun `fetchSongs - execute`() {
        `when songs are fetched`()
        `then interactor should be executed`()
        `then interactor should have no more interactions`()
    }

    @Test
    fun `fetchSongs - success`() {
        `when fetched songs has result`(Result.OnSuccess(songs))
        `then state observer should receive state`(
            AlbumDetailViewModel.State.SongsLoaded(songs)
        )
    }

    @Test
    fun `fetchSongs - error`() {
        `when fetched songs has result`(Result.OnError)
        `then state observer should receive state`(AlbumDetailViewModel.State.ShowError)
    }

    private fun `when songs are fetched`() {
        subject.fetchSongs()
    }

    private fun `when fetched songs has result`(result: Result) {
        getSongInteractorLiveData.value = result
    }

    private fun `then interactor should be executed`() {
        BDDMockito.then(getAlbumInteractor).should().execute(ALBUM_ID)
    }

    private fun `then interactor should have no more interactions`() {
        BDDMockito.then(getAlbumInteractor).should().getLiveData()
        BDDMockito.then(getAlbumInteractor).shouldHaveNoMoreInteractions()
    }

    private fun `then state observer should receive state`(state: AlbumDetailViewModel.State) {
        BDDMockito.then(observer).should().onChanged(state)
        BDDMockito.then(observer).shouldHaveNoMoreInteractions()
    }

    companion object {
        private const val ALBUM_ID = 0
    }
}
