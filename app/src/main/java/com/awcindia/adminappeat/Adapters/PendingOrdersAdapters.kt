package com.awcindia.adminappeat.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.awcindia.adminappeat.databinding.OrderPendingBinding
import com.bumptech.glide.Glide

class PendingOrdersAdapters (private val context: Context,
                             private val customerName: MutableList<String>,
                             private val totalPrice: MutableList<String>,
                             private val foodImageList: MutableList<String>,
                             private val itemClicked: OnItemClicked
) : RecyclerView.Adapter<PendingOrdersAdapters.PendingOrderViewHolder>() {


    interface OnItemClicked {
        fun onItemClickListener(position: Int)
        fun onItemAcceptListener(position: Int)
        fun onItemDispatchListener(position: Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding =
            OrderPendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrdersAdapters.PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerName.size

    inner class PendingOrderViewHolder(private val binding: OrderPendingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false

        fun bind(position: Int) {
            binding.apply {
                customerName.text = this@PendingOrdersAdapters.customerName[position]
                totalPrice.text = this@PendingOrdersAdapters.totalPrice[position]
                val uriSting = foodImageList[position]
                val uri = Uri.parse(uriSting)
                Glide.with(context).load(uri).into(orderImage)

                customerName.setOnClickListener {
                    itemClicked.onItemClickListener(position)
                }


                orderAccepted.apply {
                    if (!isAccepted) {
                        text = "Accept"
                    } else {
                        text = "Dispatch"
                    }

                    setOnClickListener {
                        if (!isAccepted) {
                            text = "Dispatch"
                            isAccepted = true
                            itemClicked.onItemAcceptListener(position)
                            Toast.makeText(context, "The Order is Accepted", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            this@PendingOrdersAdapters.customerName.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            itemClicked.onItemDispatchListener(position)
                            Toast.makeText(context, "The Order is Dispatch", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }
}