package com.testlbc.ui.albumlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.testlbc.core.EventPath
import com.testlbc.core.EventPathObserver
import com.testlbc.data.interactor.GetAlbumsInteractor
import com.testlbc.data.interactor.GetAlbumsInteractor.Result
import com.testlbc.ui.albumlist.AlbumListViewModel.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AlbumListViewModelImplTest {

    @Rule
    @JvmField
    val instantTaskRule = InstantTaskExecutorRule()

    @Mock
    lateinit var getAlbumsInteractor: GetAlbumsInteractor

    @Mock
    lateinit var albums: List<AlbumVM>

    @Mock
    lateinit var stateObserver: Observer<State>

    @Mock
    lateinit var navigationObserver: EventPathObserver

    private lateinit var subject: AlbumListViewModelImpl

    private val state: MediatorLiveData<State> = MediatorLiveData()
    private val navigation: MutableLiveData<EventPath<Path>> = MutableLiveData()
    private val getAlbumsInteractorLiveData: MutableLiveData<Result> =
        MutableLiveData()

    @Before
    fun setUp() {
        setUpLiveData()
        subject = AlbumListViewModelImpl(state, navigation, getAlbumsInteractor)
    }

    private fun setUpLiveData() {
        state.observeForever(stateObserver)
        navigation.observeForever(navigationObserver)
        given(getAlbumsInteractor.getLiveData()).willReturn(getAlbumsInteractorLiveData)
    }

    @Test
    fun `fetchAlbums - execute`() {
        `when albums are fetched`()
        `then interactor should be executed`()
        `then interactor should have no more interactions`()
    }

    @Test
    fun `fetchAlbums - success`() {
        `when fetched albums has result`(Result.OnSuccess(albums))
        `then state observer should receive state`(State.AlbumsLoaded(albums))
    }

    @Test
    fun `fetchAlbums - error`() {
        `when fetched albums has result`(Result.OnError)
        `then state observer should receive state`(State.ShowError)
    }

    private fun `when albums are fetched`() {
        subject.fetchAlbums()
    }

    private fun `when fetched albums has result`(result: Result) {
        getAlbumsInteractorLiveData.value = result
    }

    private fun `then interactor should be executed`() {
        then(getAlbumsInteractor).should().execute()
    }

    private fun `then interactor should have no more interactions`() {
        then(getAlbumsInteractor).should().getLiveData()
        then(getAlbumsInteractor).shouldHaveNoMoreInteractions()
    }

    private fun `then state observer should receive state`(state: State) {
        then(stateObserver).should().onChanged(state)
        then(stateObserver).shouldHaveNoMoreInteractions()
    }
}
