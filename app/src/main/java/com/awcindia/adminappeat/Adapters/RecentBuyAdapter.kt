package com.awcindia.adminappeat.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awcindia.adminappeat.databinding.RecentBuyItemBinding
import com.bumptech.glide.Glide

class RecentBuyAdapter(
    private var context: Context,
    private val foodNameList: MutableList<String>,
    private val foodPriceList: MutableList<String>,
    private val foodImageList: MutableList<String>,
    private val foodQuantities: MutableList<Int>,
) : RecyclerView.Adapter<RecentBuyAdapter.RecentBuyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecentBuyAdapter.RecentBuyViewHolder {
        val binding =
            RecentBuyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentBuyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentBuyAdapter.RecentBuyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = foodNameList.size

    inner class RecentBuyViewHolder(private val binding: RecentBuyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            binding.apply {
                foodname.text = foodNameList[position]
                foodPrice.text = foodPriceList[position]
                foodquantity.text = foodQuantities[position].toString()
                val uriSting = foodImageList[position]
                val uri = Uri.parse(uriSting)
                Glide.with(context).load(uri).into(foodImage)
            }
        }
    }
}
