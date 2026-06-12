package com.test.mandiri.news.feature.news.categories

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.mandiri.news.domain.news.model.NewsCategory
import com.test.mandiri.news.feature.news.databinding.ItemCategoryBinding

class CategoriesAdapter(
    private val onItemClick: (NewsCategory) -> Unit
) : ListAdapter<NewsCategory, CategoriesAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: NewsCategory) {
            binding.tvEmoji.text = category.emoji
            binding.tvCategoryName.text = category.displayName
            try {
                binding.root.setCardBackgroundColor(Color.parseColor(category.colorHex))
            } catch (_: IllegalArgumentException) { }
            binding.root.setOnClickListener { onItemClick(category) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<NewsCategory>() {
        override fun areItemsTheSame(oldItem: NewsCategory, newItem: NewsCategory) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NewsCategory, newItem: NewsCategory) =
            oldItem == newItem
    }
}
