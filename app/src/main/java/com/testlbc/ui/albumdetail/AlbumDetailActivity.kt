package com.testlbc.ui.albumdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.testlbc.core.EventPathObserver
import com.testlbc.databinding.ActivityAlbumDetailBinding
import com.testlbc.ui.albumdetail.AlbumDetailViewModel.Path
import com.testlbc.ui.albumdetail.AlbumDetailViewModel.State
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AlbumDetailActivity : AppCompatActivity() {

    private val albumId by lazy { intent.getIntExtra(EXTRA_ALBUM_ID, 0) }

    private val viewModel: AlbumDetailViewModel by viewModel { parametersOf(albumId) }

    private val binding by lazy { ActivityAlbumDetailBinding.inflate(layoutInflater) }

    private val adapter by lazy { AlbumDetailAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.getState().observe(this, ::applyState)
        viewModel.getNavigation().observe(this, EventPathObserver { navigateTo(it as Path) })

        binding.songList.layoutManager = LinearLayoutManager(this)
        binding.songList.adapter = adapter

        viewModel.fetchSongs()
    }

    private fun applyState(state: State) = when (state) {
        is State.SongsLoaded -> adapter.items = state.items
        State.ShowError -> {
            // TODO: show error
        }
    }

    private fun navigateTo(path: Path) = when (path) {
        is Path.SongDetail -> openSongDetail(path.songId)
    }

    private fun openSongDetail(songId: Int) {
        // TODO:
    }

    companion object {
        private const val EXTRA_ALBUM_ID = "extra:albumId"

        fun newIntent(context: Context, albumId: Int): Intent {
            return Intent(context, AlbumDetailActivity::class.java).apply {
                putExtra(EXTRA_ALBUM_ID, albumId)
            }
        }
    }
}
