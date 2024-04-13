package com.awcindia.adminappeat.Adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awcindia.adminappeat.databinding.DeliveryItemBinding

class deliveryAdapter(private val customerNames: MutableList<String>, private val moneyStatus: MutableList<String>) : RecyclerView.Adapter<deliveryAdapter.deliveryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): deliveryViewHolder {
        val binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return deliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: deliveryAdapter.deliveryViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size

    inner class deliveryViewHolder(private val binding: DeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(position: Int) {
            binding.apply {

                customerName.text = customerNames[position]
                payment.text = moneyStatus[position]

                val colorMap = mapOf("Receiverd" to Color.GREEN , "Not Received" to Color.RED , "Panding" to Color.GRAY)

                payment.setTextColor(colorMap[moneyStatus[position]]?:Color.BLACK)

                statusColor.backgroundTintList = ColorStateList.valueOf(colorMap[moneyStatus[position]]?:Color.BLACK)

            }
        }
    }
}