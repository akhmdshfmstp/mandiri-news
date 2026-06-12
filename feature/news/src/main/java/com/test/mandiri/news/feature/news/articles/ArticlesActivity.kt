package com.test.mandiri.news.feature.news.articles

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
import com.test.mandiri.news.feature.news.articledetail.ArticleDetailActivity
import com.test.mandiri.news.feature.news.databinding.ActivityArticlesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArticlesActivity : BaseActivity<ActivityArticlesBinding>() {

    override fun getViewBinding() = ActivityArticlesBinding.inflate(layoutInflater)

    private val viewModel: ArticlesViewModel by viewModels()

    @Inject
    lateinit var router: Router

    private val adapter = ArticlesAdapter { article ->
        router.navigate(
            context = this,
            path = "/news/article-detail",
            query = mapOf(
                ArticleDetailActivity.EXTRA_URL to article.url,
                ArticleDetailActivity.EXTRA_TITLE to article.title,
            ),
            navigator = { startActivity(it) },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sourceId = intent.getStringExtra(EXTRA_SOURCE_ID) ?: ""
        val sourceName = intent.getStringExtra(EXTRA_SOURCE_NAME) ?: "Articles"

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = sourceName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        setupSwipeRefresh()
        observeUiState()

        viewModel.loadArticles(sourceId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.action_search)?.actionView as? SearchView
        searchView?.queryHint = getString(R.string.hint_search_articles)
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
        binding.rvArticles.layoutManager = LinearLayoutManager(this)
        binding.rvArticles.adapter = adapter
        binding.rvArticles.addLoadMoreListener { viewModel.loadMore() }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }

    private fun observeUiState() {
        collectFlow(viewModel.uiState) { state ->
            binding.swipeRefresh.isRefreshing = false

            when {
                state.isLoading -> showLoading()
                state.error != null && state.articles.isEmpty() -> showError(state.error)
                state.articles.isEmpty() -> showEmpty()
                else -> showContent(state)
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvArticles.visibility = View.GONE
        binding.layoutEmpty.visibility = View.GONE
        binding.layoutError.visibility = View.GONE
    }

    private fun showContent(state: ArticlesUiState) {
        binding.progressBar.visibility = View.GONE
        binding.rvArticles.visibility = View.VISIBLE
        binding.layoutEmpty.visibility = View.GONE
        binding.layoutError.visibility = View.GONE

        val items = state.articles.map { ArticleItem.Content(it) } +
            if (state.isLoadingMore) listOf(ArticleItem.Loading) else emptyList()
        adapter.submitList(items)
    }

    private fun showEmpty() {
        binding.progressBar.visibility = View.GONE
        binding.rvArticles.visibility = View.GONE
        binding.layoutEmpty.visibility = View.VISIBLE
        binding.layoutError.visibility = View.GONE
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.rvArticles.visibility = View.GONE
        binding.layoutEmpty.visibility = View.GONE
        binding.layoutError.visibility = View.VISIBLE
        binding.tvError.text = message
        binding.btnRetry.setOnClickListener { viewModel.refresh() }
    }

    companion object {
        const val EXTRA_SOURCE_ID = "extra_source_id"
        const val EXTRA_SOURCE_NAME = "extra_source_name"
    }
}
