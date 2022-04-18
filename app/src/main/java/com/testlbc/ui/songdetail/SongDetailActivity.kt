package com.testlbc.ui.songdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.squareup.picasso.Picasso
import com.testlbc.R
import com.testlbc.core.BaseActivity
import com.testlbc.data.repository.local.Song
import com.testlbc.databinding.ActivitySongDetailBinding
import com.testlbc.ui.songdetail.SongDetailViewModel.State
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SongDetailActivity : BaseActivity() {

    private val songId by lazy { intent.getIntExtra(EXTRA_SONG_ID, 0) }

    private val viewModel: SongDetailViewModel by viewModel { parametersOf(songId) }

    private val binding by lazy { ActivitySongDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.getState().observe(this, ::applyState)

        viewModel.fetchSong()
    }

    private fun applyState(state: State) = when (state) {
        is State.SongLoaded -> displaySong(state.song)
        State.ShowError -> {
            // TODO: showError
        }
    }

    private fun displaySong(song: Song) {
        Picasso.get()
            .load(song.url)
            .placeholder(R.drawable.placeholder_song)
            .into(binding.songCover)

        binding.songTitle.text = song.title
    }

    companion object {
        private const val EXTRA_SONG_ID = "extra:songId"

        fun newIntent(context: Context, songId: Int): Intent {
            return Intent(context, SongDetailActivity::class.java).apply {
                putExtra(EXTRA_SONG_ID, songId)
            }
        }
    }
}
