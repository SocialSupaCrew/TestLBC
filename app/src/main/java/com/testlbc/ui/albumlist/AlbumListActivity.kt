package com.testlbc.ui.albumlist

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.testlbc.core.BaseActivity
import com.testlbc.databinding.ActivityAlbumListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumListActivity : BaseActivity() {

    private val viewModel: AlbumListViewModel by viewModel()

    private val binding by lazy { ActivityAlbumListBinding.inflate(layoutInflater) }

    private val adapter by lazy { AlbumListAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.albumList.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.albumList.adapter = adapter

        viewModel.getState().observe(this, ::applyState)

        viewModel.fetchAlbums()
    }

    private fun applyState(state: AlbumListViewModel.State) = when (state) {
        is AlbumListViewModel.State.AlbumsLoaded -> adapter.items = state.items
        AlbumListViewModel.State.ShowError -> showError()
        AlbumListViewModel.State.ShowLoading -> {
            // TODO: show loading
        }
    }

    private fun showError() {
        Snackbar.make(binding.root, "Error while loading albums", Snackbar.LENGTH_LONG).show()
    }
}
