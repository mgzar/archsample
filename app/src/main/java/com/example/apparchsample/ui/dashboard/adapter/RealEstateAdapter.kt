package com.example.apparchsample.ui.dashboard.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.apparchsample.R
import com.example.apparchsample.databinding.RealestateRecyclerItemBinding
import com.example.apparchsample.network.dtos.RealEstate


class RealEstateAdapter(var callBackData: (RealEstate) -> Unit) :
    RecyclerView.Adapter<RealEstateRecViewHolder>() {

    private var realEstate: List<RealEstate> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(realEstateList: List<RealEstate>) {
        realEstate = realEstateList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealEstateRecViewHolder {
        val withDataBinding: RealestateRecyclerItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            RealEstateRecViewHolder.LAYOUT,
            parent,
            false
        )
        return RealEstateRecViewHolder(withDataBinding)
    }

    override fun getItemCount() = realEstate.size

    override fun onBindViewHolder(holder: RealEstateRecViewHolder, position: Int) {
        holder.bind(realEstate[position])
        holder.viewDataBinding.videoThumbnail.setOnClickListener {
            callBackData.invoke(realEstate[position])
        }
    }
}

class RealEstateRecViewHolder(val viewDataBinding: RealestateRecyclerItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    fun bind(data: RealEstate) {
        viewDataBinding.data = data
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.realestate_recycler_item
    }
}