package com.test.mandiri.news.domain.news.model

data class NewsCategory(
    val id: String,
    val displayName: String,
    val emoji: String,
    val colorHex: String
)

object NewsCategories {
    val all = listOf(
        NewsCategory("business", "Business", "💼", "#1976D2"),
        NewsCategory("entertainment", "Entertainment", "🎬", "#E91E63"),
        NewsCategory("general", "General", "📰", "#607D8B"),
        NewsCategory("health", "Health", "🏥", "#4CAF50"),
        NewsCategory("science", "Science", "🔬", "#9C27B0"),
        NewsCategory("sports", "Sports", "⚽", "#FF5722"),
        NewsCategory("technology", "Technology", "💻", "#00BCD4"),
    )
}
