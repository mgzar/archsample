package com.example.apparchsample.util

import android.annotation.SuppressLint
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.apparchsample.R
import java.io.File

@BindingAdapter("isNetworkError", "playlist")
fun hideIfNetworkError(view: View, isNetWorkError: Boolean, playlist: Any?) {
    view.visibility = if (playlist != null) View.GONE else View.VISIBLE
    if (isNetWorkError) {
        view.visibility = View.GONE
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("counter", "totalCount")
fun setCounter(view: TextView, counter:Int,totalCount:Int) {
    view.text = "$counter/$totalCount"
}



@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    url?.let {
        val storageDir = "/storage/emulated/0/"
        val folderName = "/Sunrise/"
        val name = url.substringAfterLast("/")
        val file = File(storageDir + Environment.DIRECTORY_DOCUMENTS + folderName, name)
        val extensionName = name.trim().split(".")
        Glide.with(imageView.context).load(
            if (extensionName.size >= 2) {
                if (extensionName[1] != "pdf"){
                    file
                }else{
                    R.drawable.ic_baseline_picture_as_pdf_24
                }
            } else {
                R.drawable.ic_baseline_broken_image_24
            }
        ).diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }
}