package com.testlbc.data.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.socialsupacrew.testlbc.rule.SchedulerRule
import com.testlbc.data.interactor.GetAlbumsInteractor.Result
import com.testlbc.data.repository.SongRepository
import com.testlbc.data.repository.local.Song
import com.testlbc.ui.albumlist.AlbumListViewModel.AlbumVM
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAlbumsInteractorImplTest {

    @Rule
    @JvmField
    val instantTaskRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val schedulerRule = SchedulerRule()

    @Mock
    private lateinit var repository: SongRepository

    @Mock
    private lateinit var mapper: AlbumMapper

    @Mock
    private lateinit var songs: List<Song>

    @Mock
    private lateinit var albums: List<AlbumVM>

    @Mock
    private lateinit var throwable: Throwable

    private lateinit var subject: GetAlbumsInteractorImpl

    @Before
    fun setUp() {
        subject = GetAlbumsInteractorImpl(repository, mapper)
    }

    @Test
    fun `execute - success`() {
        `given repository`(Flowable.just(songs))
        `given mapping`(albums)
        `when interactor is executed`()
        `then live data should have result`(Result.OnSuccess(albums))
    }

    @Test
    fun `execute - error`() {
        `given repository`(Flowable.error(throwable))
        `when interactor is executed`()
        `then live data should have result`(Result.OnError)
    }

    private fun `given repository`(flowable: Flowable<List<Song>>) {
        given(repository.get()).willReturn(flowable)
    }

    private fun `given mapping`(albums: List<AlbumVM>) {
        given(mapper.map(songs)).willReturn(albums)
    }

    private fun `when interactor is executed`() {
        subject.execute()
    }

    private fun `then live data should have result`(result: Result) {
        assertThat(subject.getLiveData().value).isEqualTo(result)
    }
}
