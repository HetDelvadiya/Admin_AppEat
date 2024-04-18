package com.awcindia.adminappeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.awcindia.adminappeat.Adapters.deliveryAdapter
import com.awcindia.adminappeat.Modals.OrderDetails
import com.awcindia.adminappeat.databinding.ActivityOutOfDeliveryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutOfDeliveryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOutOfDeliveryBinding

    private lateinit var database: FirebaseDatabase
    private  var listOfCompleteOrderList : ArrayList<OrderDetails> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutOfDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        // Retrieve and Display Completed Order

        retrieveCompleteOrderDetails()



    }

    private fun retrieveCompleteOrderDetails() {
        // Initialize Firebase database

        database = FirebaseDatabase.getInstance()
        val completeOrderReference = database.reference.child("CompletedOrder").orderByChild("currentTime")
        completeOrderReference.addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear List
                listOfCompleteOrderList.clear()
                for (orderSnapshot in snapshot.children){
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it)
                    }
                }
                listOfCompleteOrderList.reverse()
                setDataIntoRecyclerview()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun setDataIntoRecyclerview() {
        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()


        for (order in listOfCompleteOrderList){
            order.userName?.let {
                customerName.add(it)
            }

            moneyStatus.add(order.paymentReceived)
        }

        val adapter = deliveryAdapter(customerName , moneyStatus )
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cartRecyclerView.adapter = adapter
    }
}