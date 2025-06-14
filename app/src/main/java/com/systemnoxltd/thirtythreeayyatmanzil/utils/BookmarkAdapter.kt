package com.systemnoxltd.thirtythreeayyatmanzil.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.systemnoxltd.thirtythreeayyatmanzil.R

class BookmarkAdapter(
    private val bookmarks: MutableList<Pair<Int, String>>,
    private val onItemClick: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    inner class BookmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.bookmarkTitle)
        val page = itemView.findViewById<TextView>(R.id.bookmarkPage)
        val deleteIcon = itemView.findViewById<ImageView>(R.id.deleteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false)
        return BookmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val (pageNumber, titleText) = bookmarks[position]
        holder.title.text = titleText
        holder.page.text = "Page $pageNumber"

        holder.itemView.setOnClickListener {
            onItemClick(pageNumber)
        }
        holder.deleteIcon.setOnClickListener {
            onDelete(pageNumber)
        }
    }

    override fun getItemCount() = bookmarks.size
}
