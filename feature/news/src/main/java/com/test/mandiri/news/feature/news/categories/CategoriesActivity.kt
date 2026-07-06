package com.test.mandiri.news.feature.news.categories

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.test.mandiri.news.common.base.BaseActivity
import com.test.mandiri.news.common.extension.collectFlow
import com.test.mandiri.news.core.router.api.Router
import com.test.mandiri.news.core.router.impl.navigate
import com.test.mandiri.news.feature.news.databinding.ActivityCategoriesBinding
import com.test.mandiri.news.feature.news.sources.SourcesActivity
import com.test.mandiri.news.feature.news.sources.SourcesRouteHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesActivity : BaseActivity<ActivityCategoriesBinding>() {

    override fun getViewBinding() = ActivityCategoriesBinding.inflate(layoutInflater)

    private val viewModel: CategoriesViewModel by viewModels()

    @Inject
    lateinit var router: Router

    private val adapter = CategoriesAdapter { category ->
        router.navigate(
            context = this,
            path = SourcesRouteHandler.PATH,
            query = mapOf(
                SourcesActivity.EXTRA_CATEGORY_ID to category.id,
                SourcesActivity.EXTRA_CATEGORY_NAME to category.displayName,
            ),
            navigator = { startActivity(it) },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        setupRecyclerView()
        collectFlow(viewModel.categories) { categories ->
            adapter.submitList(categories)
        }
    }

    private fun setupRecyclerView() {
        binding.rvCategories.layoutManager = GridLayoutManager(this, 2)
        binding.rvCategories.adapter = adapter
    }
}
