package com.testlbc.ui.albumlist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.testlbc.core.BaseAdapter
import com.testlbc.ui.albumlist.AlbumListHolder.Listener
import com.testlbc.ui.albumlist.AlbumListViewModel.AlbumVM

class AlbumListAdapter(
    private val listener: Listener
) : BaseAdapter<AlbumVM, AlbumListHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumListHolder {
        return AlbumListHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: AlbumListHolder, position: Int) {
        holder.bind(items[position])
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AlbumVM>() {
            override fun areItemsTheSame(oldItem: AlbumVM, newItem: AlbumVM): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AlbumVM, newItem: AlbumVM): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}
