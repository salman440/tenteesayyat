package com.systemnoxltd.thirtythreeayyatmanzil.utils

import android.content.Context

//object BookmarkManager {
//    private const val PREF_NAME = "Bookmarks"
//    private const val KEY = "bookmark_pages"
//
//    fun addBookmark(context: Context, page: Int) {
//        val set = getBookmarks(context).toMutableSet()
//        set.add(page.toString())
//        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//            .edit().putStringSet(KEY, set).apply()
//    }
//
//    fun getBookmarks(context: Context): Set<String> {
//        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//            .getStringSet(KEY, emptySet()) ?: emptySet()
//    }
//
//    fun removeBookmark(context: Context, page: Int) {
//        val set = getBookmarks(context).toMutableSet()
//        set.remove(page.toString())
//        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//            .edit().putStringSet(KEY, set).apply()
//    }
//
//    fun clearAll(context: Context) {
//        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply()
//    }
//}

// BookmarkManager.kt
object BookmarkManager {
    private const val PREF_NAME = "bookmarks"

    fun addBookmark(context: Context, page: Int, title: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(page.toString(), title)
        editor.apply()
    }

    fun getBookmarks(context: Context): List<Pair<Int, String>> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.all.mapNotNull {
            val page = it.key.toIntOrNull()
            val title = it.value as? String
            if (page != null && title != null) page to title else null
        }.sortedBy { it.first }
    }

    fun removeBookmark(context: Context, page: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(page.toString()).apply()
    }

    fun clearAll(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().clear().apply()
    }
}

