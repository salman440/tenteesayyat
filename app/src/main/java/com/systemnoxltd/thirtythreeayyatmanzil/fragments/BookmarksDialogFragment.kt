package com.systemnoxltd.thirtythreeayyatmanzil.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.systemnoxltd.thirtythreeayyatmanzil.MainActivity
import com.systemnoxltd.thirtythreeayyatmanzil.R
import com.systemnoxltd.thirtythreeayyatmanzil.utils.BookmarkAdapter
import com.systemnoxltd.thirtythreeayyatmanzil.utils.BookmarkManager

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [BookmarksDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookmarksDialogFragment : DialogFragment() {

    interface OnBookmarkSelectedListener {
        fun onBookmarkSelected(pageNumber: Int)
    }

    var listener: OnBookmarkSelectedListener? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookmarkAdapter
    private val bookmarks = mutableListOf<Pair<Int, String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_bookmarks_dialog, container, false)

        recyclerView = view.findViewById(R.id.bookmarksRecyclerView)
        val clearAll = view.findViewById<TextView>(R.id.clearAll)
        val emptyView = view.findViewById<TextView>(R.id.emptyView)

        bookmarks.addAll(BookmarkManager.getBookmarks(requireContext()))

        adapter = BookmarkAdapter(
            bookmarks,
            onItemClick = { pageNumber ->
//                val intent = Intent(requireContext(), MainActivity::class.java)
//                intent.putExtra("page", pageNumber)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//                dismiss()
                listener?.onBookmarkSelected(pageNumber)
                dismiss()
            },
            onDelete = { pageToDelete ->
                BookmarkManager.removeBookmark(requireContext(), pageToDelete)
                bookmarks.clear()
                bookmarks.addAll(BookmarkManager.getBookmarks(requireContext()))
                adapter.notifyDataSetChanged()
                emptyView.visibility = if (bookmarks.isEmpty()) View.VISIBLE else View.GONE
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        emptyView.visibility = if (bookmarks.isEmpty()) View.VISIBLE else View.GONE

        clearAll.setOnClickListener {
            BookmarkManager.clearAll(requireContext())
            bookmarks.clear()
            adapter.notifyDataSetChanged()
            emptyView.visibility = View.VISIBLE
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}