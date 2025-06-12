package com.systemnoxltd.thirtythreeayyatmanzil

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.systemnoxltd.thirtythreeayyatmanzil.utils.BookmarkManager

class BookmarksActivity : AppCompatActivity() {
    private lateinit var bookmarkList: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val bookmarks = mutableListOf<Pair<Int, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmarks)

        bookmarkList = findViewById(R.id.bookmarkList)
        bookmarks.addAll(BookmarkManager.getBookmarks(this))
        adapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1,
            bookmarks.map { "Page ${it.first + 1}: ${it.second}" })
        bookmarkList.adapter = adapter

        bookmarkList.setOnItemClickListener { _, _, position, _ ->
            val page = bookmarks[position].first
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("page", page)
            startActivity(intent)
        }

        bookmarkList.setOnItemLongClickListener { _, _, position, _ ->
            val page = bookmarks[position].first
            BookmarkManager.removeBookmark(this, page)
            bookmarks.removeAt(position)
            adapter.clear()
            adapter.addAll(bookmarks.map { "Page ${it.first + 1}: ${it.second}" })
            true
        }

        findViewById<Button>(R.id.clearBtn).setOnClickListener {
            BookmarkManager.clearAll(this)
            bookmarks.clear()
            adapter.clear()
        }
    }

}