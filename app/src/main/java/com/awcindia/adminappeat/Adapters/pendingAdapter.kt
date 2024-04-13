package com.awcindia.adminappeat.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.awcindia.adminappeat.databinding.OrderPendingBinding

class PendingAdapter(private val customer_Name : ArrayList<String>, private val quantitys : ArrayList<String>, private val foodImage : ArrayList<Int>) : RecyclerView.Adapter<PendingAdapter.PendingOrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding = OrderPendingBinding.inflate(LayoutInflater.from(parent.context) ,parent , false )
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingAdapter.PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customer_Name.size

   inner class PendingOrderViewHolder(private val binding: OrderPendingBinding) : RecyclerView.ViewHolder(binding.root) {
       private var isAccepted = false

       fun bind(position: Int) {
            binding.apply {
                customerName.text = customer_Name[position]
                quantity.text = quantitys[position]
                orderImage.setImageResource(foodImage[position])

                orderAccepted.apply {
                    if (!isAccepted){
                        text = "Accept"
                    }else{
                        text = "Dispatch"
                    }

                    setOnClickListener {
                        if (!isAccepted){
                            text = "Dispatch"
                            isAccepted = true
                            Toast.makeText(context , "The Order is Accepted" , Toast.LENGTH_SHORT).show()
                        }
                        else{
                            customer_Name.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            Toast.makeText(context , "The Order is Dispatch" , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


    }
}