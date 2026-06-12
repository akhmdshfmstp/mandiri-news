package com.test.mandiri.news.common.extension

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addLoadMoreListener(threshold: Int = 3, onLoadMore: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val lm = layoutManager as? LinearLayoutManager ?: return
            if (lm.findLastVisibleItemPosition() >= lm.itemCount - threshold) onLoadMore()
        }
    })
}
