package com.testlbc.ui.albumlist

import android.os.Bundle
import android.util.Log
import com.testlbc.core.BaseActivity
import com.testlbc.data.repository.SongRepository
import com.testlbc.databinding.ActivityAlbumListBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

class AlbumListActivity : BaseActivity() {

    private val binding by lazy { ActivityAlbumListBinding.inflate(layoutInflater) }

    private val repository: SongRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}
