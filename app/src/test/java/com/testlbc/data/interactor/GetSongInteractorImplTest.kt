package com.testlbc.data.interactor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.testlbc.data.interactor.GetSongInteractor.Result
import com.testlbc.data.repository.SongRepository
import com.testlbc.data.repository.local.Song
import com.testlbc.rules.MainCoroutineRule
import com.testlbc.utils.assertLiveData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetSongInteractorImplTest {

    @Rule
    @JvmField
    val instantTaskRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository: SongRepository = mockk()
    private val song: Song = Song(1, 1, "", "", "")
    private val exception = Exception()

    private lateinit var subject: GetSongInteractorImpl

    @Before
    fun setUp() {
        subject = GetSongInteractorImpl(repository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `execute - success`() = runTest {
        `given repository success`()
        `when interactor is executed`()
        `then live data should have result`(Result.OnSuccess(song))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `execute - error`() = runTest {
        `given repository error`()
        `when interactor is executed`()
        `then live data should have result`(Result.OnError)
    }

    private fun `given repository success`() {
        coEvery { repository.getSong(SONG_ID) } returns song
    }

    private fun `given repository error`() {
        coEvery { repository.getSong(SONG_ID) } throws exception
    }

    @ExperimentalCoroutinesApi
    private fun `when interactor is executed`() = runTest {
        subject.execute(SONG_ID)
    }

    private fun `then live data should have result`(result: Result) {
        assertLiveData(subject.getLiveData()).isEqualTo(result)
    }

    companion object {
        private const val SONG_ID = 0
    }
}
