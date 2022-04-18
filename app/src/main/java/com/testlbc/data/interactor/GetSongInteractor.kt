package com.testlbc.data.interactor

import com.testlbc.core.domain.BaseInteractor
import com.testlbc.core.domain.Interactor
import com.testlbc.data.interactor.GetSongInteractor.Result
import com.testlbc.data.repository.SongRepository
import com.testlbc.data.repository.local.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

interface GetSongInteractor : Interactor<Result> {

    sealed class Result {
        data class OnSuccess(val song: Song) : Result()
        object OnError : Result()
    }

    suspend fun execute(songId: Int)
}

class GetSongInteractorImpl(
    private val repository: SongRepository
) : BaseInteractor<Result>(), GetSongInteractor {

    override suspend fun execute(songId: Int) {
        withContext(Dispatchers.Main) {
            try {
                onSuccess(repository.getSong(songId))
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    private fun onSuccess(song: Song) {
        liveData.value = Result.OnSuccess(song)
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
        liveData.value = Result.OnError
    }
}
