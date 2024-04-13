package com.awcindia.adminappeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.awcindia.adminappeat.Adapters.PendingAdapter
import com.awcindia.adminappeat.databinding.ActivityPendingBinding

class PendingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPendingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.back.setOnClickListener {
            finish()
        }

        val customername = listOf("Bargur", "sandwich", "coca")
        val orderQuantity = listOf("1", "5", "7")
        val orderImage = listOf(R.drawable.bar , R.drawable.coca , R.drawable.delf)
        val adapter = PendingAdapter(ArrayList(customername), ArrayList(orderQuantity) , ArrayList(orderImage))
        binding.pandingRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.pandingRecyclerView.adapter = adapter


    }
}