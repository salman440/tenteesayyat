package com.systemnoxltd.thirtythreeayyatmanzil

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.systemnoxltd.thirtythreeayyatmanzil.utils.BookmarkManager
import com.systemnoxltd.thirtythreeayyatmanzil.utils.PdfPageAdapter
import com.systemnoxltd.thirtythreeayyatmanzil.utils.RatePrompt
import com.systemnoxltd.thirtythreeayyatmanzil.utils.UpdateChecker
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.createBitmap

class MainActivity : AppCompatActivity() {

    private var currentPage = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var pageIndicator: TextView
    private lateinit var adapter: PdfPageAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var totalPages = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.pdfRecyclerView)
        pageIndicator = findViewById(R.id.pageIndicator)

        val pages = loadPagesFromAssets()
        totalPages = pages.size

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        adapter = PdfPageAdapter(pages)
        recyclerView.adapter = adapter

        // Scroll listener to update page number
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                val firstVisible = layoutManager.findFirstVisibleItemPosition()
                updatePageIndicator(firstVisible)
            }
        })

        updatePageIndicator(0)

        // Handle intent extra here
        val startPage = intent.getIntExtra("page", 0)
        recyclerView.scrollToPosition(startPage)

    }

    private fun updatePageIndicator(currentPage: Int) {
        this.currentPage = currentPage
        val pageInfo = "${currentPage + 1} / $totalPages"
        pageIndicator.text = pageInfo
    }

    fun jumpToPage(pageIndex: Int) {
        recyclerView.scrollToPosition(pageIndex)
    }

    fun loadPagesFromAssets(): List<Bitmap> {
        val bitmaps = mutableListOf<Bitmap>()
        try {
            val fileName = "ayyat.pdf"
            val file = File(cacheDir, fileName)
            // Copy from assets to a temp file
            assets.open(fileName).use { inputStream ->
                FileOutputStream(file).use { output ->
                    inputStream.copyTo(output)
                }
            }
            // Open PDF renderer
            val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(fileDescriptor)
            for (i in 0 until renderer.pageCount) {
                val page = renderer.openPage(i)
                // Define bitmap size based on page size
                val width = page.width * 2
                val height = page.height * 2
//                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val bitmap = createBitmap(width, height)
                // Render the page onto the bitmap
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                bitmaps.add(bitmap)
                page.close()
            }
            renderer.close()
            fileDescriptor.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmaps
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_bookmark -> {
//                BookmarkManager.addBookmark(this, currentPage + 1)
                showAddBookmarkDialog()
                return true
            }
            R.id.all_bookmarks -> startActivity(Intent(this, BookmarksActivity::class.java))
            R.id.rate_us -> RatePrompt.rateOnPlayStore(this)
        }
        return true
    }

    @Deprecated("Deprecated in Android 13+")
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        val prompted = RatePrompt.checkAndPrompt(this, onDecline = {
            super.onBackPressed()
        })
        if (!prompted) {
            super.onBackPressed() // only exit if not prompting
        }
    }

    private fun showAddBookmarkDialog() {
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)
        input.hint = "Enter bookmark title"

        val padding = resources.getDimensionPixelSize(R.dimen.dialog_padding)
        input.setPadding(padding, padding, padding, padding)

        builder.setTitle("Add Bookmark")
        builder.setMessage("Page ${currentPage + 1}")
        builder.setView(input)

        builder.setPositiveButton("Save") { _, _ ->
            val title = input.text.toString().ifBlank { "Page ${currentPage + 1}" }
            BookmarkManager.addBookmark(this, currentPage, title)
            Toast.makeText(this, "Bookmark saved", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }


}