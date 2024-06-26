package com.awcindia.adminappeat.Adapters


import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awcindia.adminappeat.Modals.AllMenu
import com.awcindia.adminappeat.databinding.ItemItemBinding
import com.bumptech.glide.Glide
import android.content.Context
import com.google.firebase.database.DatabaseReference


class MenuItemAdapter(
    private val menuList: ArrayList<AllMenu>,
    private val context: Context,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener: (position : Int) -> Unit
) : RecyclerView.Adapter<MenuItemAdapter.AllMenuViewHolder>() {



    private val itemQuantities = IntArray(menuList.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMenuViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllMenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size

    inner class AllMenuViewHolder(private val binding: ItemItemBinding) : RecyclerView.ViewHolder(binding.root) {

//

        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]

                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)
                allFoodName.text = menuItem.foodName
                allItemPrice.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(allItemFoodimage)

                deleteButton.setOnClickListener {
                    onDeleteClickListener(position)
                }
            }
        }
    }
}
