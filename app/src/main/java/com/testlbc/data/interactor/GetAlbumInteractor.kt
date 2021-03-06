package com.testlbc.data.interactor

import com.testlbc.core.domain.BaseInteractor
import com.testlbc.core.domain.Interactor
import com.testlbc.data.interactor.GetAlbumInteractor.Result
import com.testlbc.data.repository.SongRepository
import com.testlbc.data.repository.local.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import timber.log.Timber

interface GetAlbumInteractor : Interactor<Result> {

    sealed class Result {
        data class OnSuccess(val songs: List<Song>) : Result()
        object OnError : Result()
    }

    suspend fun execute(albumId: Int)
}

class GetAlbumInteractorImpl(
    private val repository: SongRepository
) : BaseInteractor<Result>(), GetAlbumInteractor {

    override suspend fun execute(albumId: Int) {
        withContext(Dispatchers.Main) {
            try {
                repository.getSongs(albumId).collect {
                    onSuccess(it)
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    private fun onSuccess(songs: List<Song>) {
        liveData.value = Result.OnSuccess(songs)
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
        liveData.value = Result.OnError
    }
}
