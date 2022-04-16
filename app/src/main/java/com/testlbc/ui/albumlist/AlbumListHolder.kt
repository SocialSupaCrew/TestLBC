package com.testlbc.ui.albumlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.testlbc.R
import com.testlbc.databinding.AlbumItemBinding
import com.testlbc.ui.albumlist.AlbumListViewModel.AlbumVM

class AlbumListHolder(
    private val binding: AlbumItemBinding,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup, listener: Listener) : this(
        AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        listener
    )

    fun bind(album: AlbumVM) {
        Picasso.get()
            .load(album.thumbnailUrl)
            .placeholder(R.drawable.placeholder_song)
            .into(binding.thumbnail)

        binding.albumName.text = album.name

        binding.root.setOnClickListener { listener.onAlbumClicked(album.id) }
    }

    interface Listener {
        fun onAlbumClicked(albumId: Int)
    }
}
