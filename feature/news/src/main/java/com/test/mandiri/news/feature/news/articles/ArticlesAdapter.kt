package com.test.mandiri.news.feature.news.articles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.test.mandiri.news.domain.news.model.NewsArticle
import com.test.mandiri.news.feature.news.databinding.ItemArticleBinding
import com.test.mandiri.news.feature.news.databinding.ItemLoadingBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ArticlesAdapter(
    private val onItemClick: (NewsArticle) -> Unit
) : ListAdapter<ArticleItem, RecyclerView.ViewHolder>(DiffCallback) {

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: NewsArticle) {
            binding.tvTitle.text = article.title
            binding.tvDescription.text = article.description.orEmpty()
            binding.tvSource.text = article.sourceName
            binding.tvPublishedAt.text = formatDate(article.publishedAt)
            if (!article.urlToImage.isNullOrBlank()) {
                binding.ivThumbnail.load(article.urlToImage) {
                    crossfade(true)
                }
            } else {
                binding.ivThumbnail.setImageDrawable(null)
            }
            binding.root.setOnClickListener { onItemClick(article) }
        }

        private fun formatDate(iso: String): String {
            return try {
                val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val output = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                output.format(input.parse(iso)!!)
            } catch (_: Exception) {
                iso.take(10)
            }
        }
    }

    inner class LoadingViewHolder(binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ArticleItem.Content -> VIEW_TYPE_ARTICLE
            is ArticleItem.Loading -> VIEW_TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ARTICLE) {
            ArticleViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            LoadingViewHolder(ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ArticleViewHolder) {
            holder.bind((getItem(position) as ArticleItem.Content).article)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<ArticleItem>() {
        override fun areItemsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
            if (oldItem is ArticleItem.Content && newItem is ArticleItem.Content) {
                return oldItem.article.url == newItem.article.url
            }
            return oldItem is ArticleItem.Loading && newItem is ArticleItem.Loading
        }

        override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem) =
            oldItem == newItem
    }

    companion object {
        const val VIEW_TYPE_ARTICLE = 0
        const val VIEW_TYPE_LOADING = 1
    }
}
