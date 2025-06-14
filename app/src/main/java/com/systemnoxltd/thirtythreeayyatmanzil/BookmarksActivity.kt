package com.systemnoxltd.thirtythreeayyatmanzil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.systemnoxltd.thirtythreeayyatmanzil.utils.BookmarkAdapter
import com.systemnoxltd.thirtythreeayyatmanzil.utils.BookmarkManager

class BookmarksActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookmarkAdapter
    private val bookmarks = mutableListOf<Pair<Int, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_bookmarks)

        recyclerView = findViewById(R.id.bookmarksRecyclerView)

        bookmarks.addAll(BookmarkManager.getBookmarks(this))
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
        adapter = BookmarkAdapter(
            bookmarks,
            onItemClick = { pageNumber ->
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("page", pageNumber)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            },
            onDelete = { pageToDelete ->
                BookmarkManager.removeBookmark(this, pageToDelete)
                bookmarks.clear()
                bookmarks.addAll(BookmarkManager.getBookmarks(this))
                adapter.notifyDataSetChanged()
                updateEmptyState()
            }
        )
        adapter.notifyDataSetChanged()
        updateEmptyState()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        findViewById<TextView>(R.id.clearAll).setOnClickListener {
            BookmarkManager.clearAll(this)
            bookmarks.clear()
            adapter.notifyDataSetChanged()
            updateEmptyState()
        }
    }

    private fun updateEmptyState() {
        val emptyView = findViewById<TextView>(R.id.emptyView)
        emptyView.visibility = if (bookmarks.isEmpty()) View.VISIBLE else View.GONE
    }

}