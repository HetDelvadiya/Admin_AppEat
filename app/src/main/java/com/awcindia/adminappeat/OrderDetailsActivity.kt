package com.awcindia.adminappeat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.awcindia.adminappeat.Adapters.RecentBuyAdapter
import com.awcindia.adminappeat.Modals.OrderDetails
import com.awcindia.adminappeat.databinding.ActivityOrderDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailsBinding



    private var userName: String? = null
    private var addressOrder: String? = null
    private var phone: String? = null
    private var totalPrices : String? = null
    private  var allFoodName: MutableList<String> = mutableListOf()
    private  var allFoodPrice: MutableList<String>  = mutableListOf()
    private  var allFoodImage: MutableList<String> = mutableListOf()
    private  var allFoodQuantity: MutableList<Int> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.back.setOnClickListener {
            finish()
        }

        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getSerializableExtra("userOrderDetails") as OrderDetails
        receivedOrderDetails?.let { orderDetail ->
            userName = receivedOrderDetails.userName
            addressOrder = receivedOrderDetails.address
            phone = receivedOrderDetails.phoneNumber
            totalPrices = receivedOrderDetails.totalPrice
            allFoodName = receivedOrderDetails.foodNames!!
            allFoodPrice = receivedOrderDetails.foodPrices!!
            allFoodQuantity = receivedOrderDetails.foodQuantities!!
            allFoodImage = receivedOrderDetails.foodImages!!

            setUserDetails()
            setAdapter()
        }
    }

    private fun setUserDetails() {

                    binding.apply {
                        name.text = userName
                        address.text = addressOrder
                        ContactNumbers.text = phone
                    }
    }

    private fun setAdapter() {
        val rv = binding.orderDetailsRecyclerView
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(this , allFoodName , allFoodPrice , allFoodImage , allFoodQuantity)
        rv.adapter = adapter
    }
}




