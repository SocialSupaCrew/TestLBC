package com.testlbc.data.interactor

import com.testlbc.core.domain.BaseInteractor
import com.testlbc.core.domain.Interactor
import com.testlbc.data.interactor.GetAlbumInteractor.Result
import com.testlbc.data.repository.SongRepository
import com.testlbc.data.repository.local.Song
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

interface GetAlbumInteractor : Interactor<Result> {

    sealed class Result {
        data class OnSuccess(val songs: List<Song>) : Result()
        object OnError : Result()
    }

    fun execute(albumId: Int)
}

class GetAlbumInteractorImpl(
    private val repository: SongRepository
) : BaseInteractor<Result>(), GetAlbumInteractor {

    override fun execute(albumId: Int) {
        repository.getSongs(albumId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onSuccess, ::onError)
            .track()
    }

    private fun onSuccess(songs: List<Song>) {
        liveData.value = Result.OnSuccess(songs)
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
        liveData.value = Result.OnError
    }
}
