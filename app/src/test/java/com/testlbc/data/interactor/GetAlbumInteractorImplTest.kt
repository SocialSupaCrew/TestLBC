package com.testlbc.data.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.socialsupacrew.testlbc.rule.SchedulerRule
import com.testlbc.data.interactor.GetAlbumInteractor.Result
import com.testlbc.data.repository.SongRepository
import com.testlbc.data.repository.local.Song
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAlbumInteractorImplTest {

    @Rule
    @JvmField
    val instantTaskRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val schedulerRule = SchedulerRule()

    @Mock
    private lateinit var repository: SongRepository

    @Mock
    private lateinit var songs: List<Song>

    @Mock
    private lateinit var throwable: Throwable

    private lateinit var subject: GetAlbumInteractorImpl

    @Before
    fun setUp() {
        subject = GetAlbumInteractorImpl(repository)
    }

    @Test
    fun `execute - success`() {
        `given repository`(Flowable.just(songs))
        `when interactor is executed`()
        `then live data should have result`(Result.OnSuccess(songs))
    }

    @Test
    fun `execute - error`() {
        `given repository`(Flowable.error(throwable))
        `when interactor is executed`()
        `then live data should have result`(Result.OnError)
    }

    private fun `given repository`(flowable: Flowable<List<Song>>) {
        BDDMockito.given(repository.getSongs(ALBUM_ID)).willReturn(flowable)
    }

    private fun `when interactor is executed`() {
        subject.execute(ALBUM_ID)
    }

    private fun `then live data should have result`(result: Result) {
        Truth.assertThat(subject.getLiveData().value).isEqualTo(result)
    }

    companion object {
        private const val ALBUM_ID = 0
    }
}
