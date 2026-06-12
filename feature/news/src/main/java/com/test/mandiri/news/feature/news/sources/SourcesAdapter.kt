package com.test.mandiri.news.feature.news.sources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.mandiri.news.domain.news.model.NewsSource
import com.test.mandiri.news.feature.news.databinding.ItemLoadingBinding
import com.test.mandiri.news.feature.news.databinding.ItemSourceBinding

class SourcesAdapter(
    private val onItemClick: (NewsSource) -> Unit
) : ListAdapter<SourceItem, RecyclerView.ViewHolder>(DiffCallback) {

    inner class SourceViewHolder(private val binding: ItemSourceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(source: NewsSource) {
            binding.tvSourceName.text = source.name
            binding.tvSourceDescription.text = source.description
            binding.tvCategory.text = source.category.replaceFirstChar { it.uppercase() }
            binding.tvCountry.text = source.country.uppercase()
            binding.root.setOnClickListener { onItemClick(source) }
        }
    }

    inner class LoadingViewHolder(binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is SourceItem.Content -> VIEW_TYPE_SOURCE
        is SourceItem.Loading -> VIEW_TYPE_LOADING
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SOURCE) {
            SourceViewHolder(ItemSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            LoadingViewHolder(ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SourceViewHolder) {
            holder.bind((getItem(position) as SourceItem.Content).source)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<SourceItem>() {
        override fun areItemsTheSame(oldItem: SourceItem, newItem: SourceItem): Boolean {
            if (oldItem is SourceItem.Content && newItem is SourceItem.Content) {
                return oldItem.source.id == newItem.source.id
            }
            return oldItem is SourceItem.Loading && newItem is SourceItem.Loading
        }

        override fun areContentsTheSame(oldItem: SourceItem, newItem: SourceItem) =
            oldItem == newItem
    }

    companion object {
        const val VIEW_TYPE_SOURCE = 0
        const val VIEW_TYPE_LOADING = 1
    }
}
