package com.testlbc.data.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.socialsupacrew.testlbc.rule.SchedulerRule
import com.testlbc.data.interactor.GetSongInteractor.Result
import com.testlbc.data.repository.SongRepository
import com.testlbc.data.repository.local.Song
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetSongInteractorImplTest {

    @Rule
    @JvmField
    val instantTaskRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val schedulerRule = SchedulerRule()

    @Mock
    private lateinit var repository: SongRepository

    @Mock
    private lateinit var song: Song

    @Mock
    private lateinit var throwable: Throwable

    private lateinit var subject: GetSongInteractorImpl

    @Before
    fun setUp() {
        subject = GetSongInteractorImpl(repository)
    }

    @Test
    fun `execute - success`() {
        `given repository`(Single.just(song))
        `when interactor is executed`()
        `then live data should have result`(Result.OnSuccess(song))
    }

    @Test
    fun `execute - error`() {
        `given repository`(Single.error(throwable))
        `when interactor is executed`()
        `then live data should have result`(Result.OnError)
    }

    private fun `given repository`(single: Single<Song>) {
        BDDMockito.given(repository.getSong(SONG_ID)).willReturn(single)
    }

    private fun `when interactor is executed`() {
        subject.execute(SONG_ID)
    }

    private fun `then live data should have result`(result: Result) {
        Truth.assertThat(subject.getLiveData().value).isEqualTo(result)
    }

    companion object {
        private const val SONG_ID = 0
    }
}
