package com.awcindia.adminappeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.awcindia.adminappeat.Adapters.deliveryAdapter
import com.awcindia.adminappeat.databinding.ActivityOutOfDeliveryBinding

class OutOfDeliveryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOutOfDeliveryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutOfDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        val customername = listOf("Bargur" , "sandwich" , "coca")
        val moneyStatus = listOf("Receiverd" , "Not Received" , "Panding" )

        val adapter = deliveryAdapter(ArrayList(customername) , ArrayList(moneyStatus))
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cartRecyclerView.adapter = adapter
    }
}