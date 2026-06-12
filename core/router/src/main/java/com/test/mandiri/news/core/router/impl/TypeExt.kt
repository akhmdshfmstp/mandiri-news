package com.test.mandiri.news.core.router.impl

internal fun String.parseType(): Any {
    toIntOrNull()?.also { return it }

    toLongOrNull()?.also { return it }

    toDoubleOrNull()?.also { return it }

    toBooleanStrictOrNull()?.also { return it }

    toIntArrayOrNull()?.also { return it }

    toLongArrayOrNull()?.also { return it }

    toDoubleArrayOrNull()?.also { return it }

    toBooleanArrayOrNull()?.also { return it }

    toStringArrayOrNull()?.also { return it }

    return this
}

private fun String.toIntArrayOrNull(): IntArray? {
    val list = toListOrNull(String::toInt)
    return list?.toIntArray()
}

private fun String.toLongArrayOrNull(): LongArray? {
    val list = toListOrNull(String::toLong)
    return list?.toLongArray()
}

private fun String.toDoubleArrayOrNull(): DoubleArray? {
    val list = toListOrNull(String::toDouble)
    return list?.toDoubleArray()
}

private fun String.toBooleanArrayOrNull(): BooleanArray? {
    val list = toListOrNull(String::toBooleanStrict)
    return list?.toBooleanArray()
}

private fun String.toStringArrayOrNull(): Array<String>? {
    val list = toListOrNull { it }
    return list?.toTypedArray()
}

private fun <T> String.toListOrNull(transform: (String) -> T): List<T>? {
    if (isBlank()) return null
    if (first() != '[' || last() != ']') return null

    return try {
        removeSurrounding(prefix = "[", suffix = "]")
            .split(",")
            .map(String::trim)
            .dropWhile(String::isBlank)
            .map(transform)
    } catch (e: Exception) {
        null
    }
}
