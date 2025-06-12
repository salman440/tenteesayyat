package com.systemnoxltd.thirtythreeayyatmanzil.utils

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.systemnoxltd.thirtythreeayyatmanzil.R

class PdfPageAdapter(private val pageList: List<Bitmap>) :
    RecyclerView.Adapter<PdfPageAdapter.PdfPageViewHolder>() {

    class PdfPageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.pageImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfPageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pdf_page, parent, false)
        return PdfPageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PdfPageViewHolder, position: Int) {
        holder.imageView.setImageBitmap(pageList[position])
    }

    override fun getItemCount(): Int = pageList.size
}
