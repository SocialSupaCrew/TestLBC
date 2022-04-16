package com.testlbc.data.interactor

import com.testlbc.core.domain.BaseInteractor
import com.testlbc.core.domain.Interactor
import com.testlbc.data.interactor.GetAlbumsInteractor.Result
import com.testlbc.data.repository.SongRepository
import com.testlbc.ui.albumlist.AlbumListViewModel.AlbumVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

interface GetAlbumsInteractor : Interactor<Result> {

    sealed class Result {
        data class OnSuccess(val albums: List<AlbumVM>) : Result()
        object OnError : Result()
    }

    fun execute()
}

class GetAlbumsInteractorImpl constructor(
    private val repository: SongRepository,
    private val mapper: AlbumMapper
) : BaseInteractor<Result>(),
    GetAlbumsInteractor {

    override fun execute() {
        repository.get()
            .map(mapper::map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::success, ::error)
            .track()
    }

    private fun success(albums: List<AlbumVM>) {
        liveData.value = Result.OnSuccess(albums)
    }

    private fun error(throwable: Throwable) {
        Timber.e(throwable)
        liveData.value = Result.OnError
    }
}
