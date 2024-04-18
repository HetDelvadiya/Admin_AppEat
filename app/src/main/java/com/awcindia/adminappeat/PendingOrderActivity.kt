package com.awcindia.adminappeat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.awcindia.adminappeat.Adapters.PendingOrdersAdapters
import com.awcindia.adminappeat.Modals.OrderDetails
import com.awcindia.adminappeat.databinding.ActivityPendingOrderBinding
import com.google.android.play.integrity.internal.i
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity(), PendingOrdersAdapters.OnItemClicked {


    private lateinit var binding: ActivityPendingOrderBinding
    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfTotalPrice: MutableList<String> = mutableListOf()
    private var listOfImageFoodOrder: MutableList<String> = mutableListOf()
    private var listOfOrderItem: MutableList<OrderDetails> = mutableListOf()

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

                binding.back.setOnClickListener {
                    finish()
                }

                database = FirebaseDatabase.getInstance()
                databaseOrderDetails = database.reference.child("OrderDetails")


                // get orderDetails

                getOrderDetails()


            }

            private fun getOrderDetails() {
                databaseOrderDetails.addListenerForSingleValueEvent(object  : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (orderSnapshot in snapshot.children){
                            val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                            orderDetails?.let {
                                listOfOrderItem.add(it)
                            }
                        }

                        addDataToListForRecyclerView()
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })


            }

            private fun setAdapter() {
                binding.pandingRecyclerView.layoutManager = LinearLayoutManager(this)
                val adapter = PendingOrdersAdapters(this , listOfName , listOfTotalPrice , listOfImageFoodOrder , this )
                binding.pandingRecyclerView.adapter = adapter
            }

            private fun addDataToListForRecyclerView() {
                for (orderItem in listOfOrderItem){

                    // Add data to Respective list for recyclerview
                    orderItem.userName?.let { listOfName.add(it) }
                    orderItem.totalPrice?.let { listOfTotalPrice.add(it) }
                    orderItem.foodImages?.filterNot { it.isEmpty() }?.forEach { listOfImageFoodOrder.add(it) }
                }
                setAdapter()
            }

            override fun onItemClickListener(position: Int) {
                val intent = Intent(this, OrderDetailsActivity::class.java)
                val userOrderDetails = listOfOrderItem[position]
                intent.putExtra("userOrderDetails" , userOrderDetails)
                startActivity(intent)

            }

    override fun onItemAcceptListener(position: Int) {
        // Handle Item Accept and Update DataBase

        val childItemPushKey = listOfOrderItem[position].itemPushKey
        val clickedItemOrderReference = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }

        clickedItemOrderReference?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptStatus(position)
    }
    override fun onItemDispatchListener(position: Int) {
        // update Order is dispatch
        val dispatchItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatch = dispatchItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        dispatch?.child("orderDispatch")?.setValue(true)
        val dispatchItemOrderReference = database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleteItemToDispatchOrder(dispatchItemPushKey)
            }
    }

    private fun deleteItemToDispatchOrder(dispatchItemPushKey: String){
        val OrderDetailsItemReference = database.reference.child("OrderDetails").child(dispatchItemPushKey)
        OrderDetailsItemReference.removeValue()

    }

    private fun updateOrderAcceptStatus(position: Int) {
        //Update User To Accept Order
        val userIdOfClickedItem = listOfOrderItem[position].userUid
        val pushKeyOfClickedItem = listOfOrderItem[position].itemPushKey
        val buyHistoryRef = database.reference.child("user").child(userIdOfClickedItem!!).child("BuyHistory").child(pushKeyOfClickedItem!!)
        buyHistoryRef.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)

    }
}

