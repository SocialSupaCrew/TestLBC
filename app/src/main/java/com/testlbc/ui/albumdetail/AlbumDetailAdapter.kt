package com.testlbc.ui.albumdetail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.testlbc.core.BaseAdapter
import com.testlbc.data.repository.local.Song

class AlbumDetailAdapter(
    private val listener: AlbumDetailHolder.Listener
) : BaseAdapter<Song, AlbumDetailHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumDetailHolder {
        return AlbumDetailHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: AlbumDetailHolder, position: Int) {
        holder.bind(items[position])
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Song>() {
            override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}
