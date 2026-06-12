package com.test.mandiri.news.feature.news.sources

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.mandiri.news.common.base.BaseActivity
import com.test.mandiri.news.common.extension.addLoadMoreListener
import com.test.mandiri.news.common.extension.collectFlow
import com.test.mandiri.news.core.router.api.Router
import com.test.mandiri.news.core.router.impl.navigate
import com.test.mandiri.news.feature.news.R
import com.test.mandiri.news.feature.news.articles.ArticlesActivity
import com.test.mandiri.news.feature.news.databinding.ActivitySourcesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SourcesActivity : BaseActivity<ActivitySourcesBinding>() {

    override fun getViewBinding() = ActivitySourcesBinding.inflate(layoutInflater)

    private val viewModel: SourcesViewModel by viewModels()

    @Inject
    lateinit var router: Router

    private val adapter = SourcesAdapter { source ->
        router.navigate(
            context = this,
            path = "/news/articles",
            query = mapOf(
                ArticlesActivity.EXTRA_SOURCE_ID to source.id,
                ArticlesActivity.EXTRA_SOURCE_NAME to source.name,
            ),
            navigator = { startActivity(it) },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val categoryId = intent.getStringExtra(EXTRA_CATEGORY_ID) ?: ""
        val categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME) ?: "Sources"

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = categoryName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        setupSwipeRefresh(categoryId)
        observeUiState()

        viewModel.loadSources(categoryId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.action_search)?.actionView as? SearchView
        searchView?.queryHint = getString(R.string.hint_search_sources)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText ?: "")
                return true
            }
        })
        return true
    }

    private fun setupRecyclerView() {
        binding.rvSources.layoutManager = LinearLayoutManager(this)
        binding.rvSources.adapter = adapter
        binding.rvSources.addLoadMoreListener { viewModel.loadMore() }
    }

    private fun setupSwipeRefresh(categoryId: String) {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadSources(categoryId)
        }
    }

    private fun observeUiState() {
        collectFlow(viewModel.uiState) { state ->
            binding.swipeRefresh.isRefreshing = false

            when {
                state.isLoading -> showLoading()
                state.error != null && state.sources.isEmpty() -> showError(state.error)
                state.sources.isEmpty() -> showEmpty()
                else -> showContent(state)
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvSources.visibility = View.GONE
        binding.layoutEmpty.visibility = View.GONE
        binding.layoutError.visibility = View.GONE
    }

    private fun showContent(state: SourcesUiState) {
        binding.progressBar.visibility = View.GONE
        binding.rvSources.visibility = View.VISIBLE
        binding.layoutEmpty.visibility = View.GONE
        binding.layoutError.visibility = View.GONE

        val items = state.sources.map { SourceItem.Content(it) } +
            if (state.isLoadingMore) listOf(SourceItem.Loading) else emptyList()
        adapter.submitList(items)
    }

    private fun showEmpty() {
        binding.progressBar.visibility = View.GONE
        binding.rvSources.visibility = View.GONE
        binding.layoutEmpty.visibility = View.VISIBLE
        binding.layoutError.visibility = View.GONE
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.rvSources.visibility = View.GONE
        binding.layoutEmpty.visibility = View.GONE
        binding.layoutError.visibility = View.VISIBLE
        binding.tvError.text = message
        binding.btnRetry.setOnClickListener {
            viewModel.loadSources(intent.getStringExtra(EXTRA_CATEGORY_ID) ?: "")
        }
    }

    companion object {
        const val EXTRA_CATEGORY_ID = "extra_category_id"
        const val EXTRA_CATEGORY_NAME = "extra_category_name"
    }
}
