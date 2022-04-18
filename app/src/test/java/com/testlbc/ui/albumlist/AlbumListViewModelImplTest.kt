package com.testlbc.ui.albumlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.testlbc.core.EventPath
import com.testlbc.data.interactor.GetAlbumsInteractor
import com.testlbc.data.interactor.GetAlbumsInteractor.Result
import com.testlbc.rules.MainCoroutineRule
import com.testlbc.ui.albumlist.AlbumListViewModel.*
import com.testlbc.utils.assertLiveData
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AlbumListViewModelImplTest {

    @Rule
    @JvmField
    val instantTaskRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val getAlbumsInteractor: GetAlbumsInteractor = mockk()
    private val albums: List<AlbumVM> = listOf(AlbumVM(1, "", "", 1))

    private lateinit var subject: AlbumListViewModelImpl

    private val state: MediatorLiveData<State> = MediatorLiveData()
    private val navigation: MutableLiveData<EventPath<Path>> = MutableLiveData()
    private val getAlbumsInteractorLiveData: MutableLiveData<Result> = MutableLiveData()

    @Before
    fun setUp() {
        setUpLiveData()
        subject = AlbumListViewModelImpl(state, navigation, getAlbumsInteractor)
    }

    private fun setUpLiveData() {
        every { getAlbumsInteractor.getLiveData() } returns getAlbumsInteractorLiveData
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetchAlbums - execute`() = runTest {
        `when albums are fetched`()
        `then interactor should be executed`()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetchAlbums - success`() = runTest {
        `when fetched albums has result`(Result.OnSuccess(albums))
        `then state observer should receive state`(State.AlbumsLoaded(albums))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetchAlbums - error`() = runTest {
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
        coVerify { getAlbumsInteractor.execute() }
    }

    private fun `then state observer should receive state`(state: State) {
        assertLiveData(subject.getState()).isEqualTo(state)
    }
}
