package com.testlbc.ui.albumlist

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.testlbc.core.BaseActivity
import com.testlbc.core.EventPathObserver
import com.testlbc.databinding.ActivityAlbumListBinding
import com.testlbc.ui.albumdetail.AlbumDetailActivity
import com.testlbc.ui.albumlist.AlbumListViewModel.Path
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumListActivity : BaseActivity() {

    private val viewModel: AlbumListViewModel by viewModel()

    private val binding by lazy { ActivityAlbumListBinding.inflate(layoutInflater) }

    private val adapter by lazy { AlbumListAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.albumList.layoutManager = LinearLayoutManager(this)
        binding.albumList.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        binding.albumList.adapter = adapter

        viewModel.getState().observe(this, ::applyState)
        viewModel.getNavigation().observe(this, EventPathObserver { navigateTo(it as Path) })

        viewModel.fetchAlbums()
    }

    private fun applyState(state: AlbumListViewModel.State) = when (state) {
        is AlbumListViewModel.State.AlbumsLoaded -> adapter.items = state.items
        AlbumListViewModel.State.ShowError -> showError()
        AlbumListViewModel.State.ShowLoading -> {
            // TODO: show loading
        }
    }

    private fun navigateTo(path: Path) = when (path) {
        is Path.AlbumDetail -> openAlbumDetail(path.albumId)
    }

    private fun showError() {
        Snackbar.make(binding.root, "Error while loading albums", Snackbar.LENGTH_LONG).show()
    }

    private fun openAlbumDetail(albumId: Int) {
        startActivity(AlbumDetailActivity.newIntent(this, albumId))
    }
}
