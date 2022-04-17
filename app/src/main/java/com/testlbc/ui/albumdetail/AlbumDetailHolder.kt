package com.testlbc.ui.albumdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.testlbc.R
import com.testlbc.data.repository.local.Song
import com.testlbc.databinding.SongItemBinding

class AlbumDetailHolder(
    private val binding: SongItemBinding,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup, listener: Listener) : this(
        SongItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        listener
    )

    fun bind(song: Song) {
        binding.songTitle.text = song.title

        Picasso.get()
            .load(song.thumbnailUrl)
            .placeholder(R.drawable.placeholder_song)
            .into(binding.thumbnail)

        binding.root.setOnClickListener { listener.onSongClicked(song.id) }
    }

    interface Listener {
        fun onSongClicked(songId: Int)
    }
}
