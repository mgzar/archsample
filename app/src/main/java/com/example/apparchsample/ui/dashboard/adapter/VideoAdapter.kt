package com.example.apparchsample.ui.dashboard.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.apparchsample.R
import com.example.apparchsample.databinding.VideoRecyclerItemBinding
import com.example.apparchsample.domain.SampleVideo

class VideoAdapter(var callBackData: (SampleVideo) -> Unit) :
    RecyclerView.Adapter<RecyclerViewHolder>() {

    private var videos: List<SampleVideo> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(videoList: List<SampleVideo>) {
        videos = videoList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val withDataBinding: VideoRecyclerItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            RecyclerViewHolder.LAYOUT,
            parent,
            false
        )
        return RecyclerViewHolder(withDataBinding)
    }

    override fun getItemCount() = videos.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(videos[position])
        holder.viewDataBinding.videoThumbnail.setOnClickListener {
            callBackData.invoke(videos[position])
        }
    }
}

class RecyclerViewHolder(val viewDataBinding: VideoRecyclerItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    fun bind(data: SampleVideo) {
        viewDataBinding.video = data
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.video_recycler_item
    }
}