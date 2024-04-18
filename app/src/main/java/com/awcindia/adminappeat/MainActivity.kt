package com.awcindia.adminappeat

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.awcindia.adminappeat.Modals.OrderDetails
import com.awcindia.adminappeat.databinding.ActivityMainBinding
import com.google.android.play.integrity.internal.w
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

open class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = FirebaseDatabase.getInstance()

        databaseReference = FirebaseDatabase.getInstance().reference.child("CompletedOrder")


        binding.addMenu.setOnClickListener {
            val intent = Intent(this, AddItem_Activity::class.java)
            startActivity(intent)
        }

        binding.allItemMenu.setOnClickListener {
            val i = Intent(this, All_Menu_Activity::class.java)
            startActivity(i)
        }

        binding.profile.setOnClickListener {
            val i = Intent(this, ProfileActivity::class.java)
            startActivity(i)
        }

        binding.orderDispatch.setOnClickListener {
            val i = Intent(this, OutOfDeliveryActivity::class.java)
            startActivity(i)
        }
        binding.createNewUser.setOnClickListener {
            val i = Intent(this, CreateUserActivity::class.java)
            startActivity(i)
        }
        binding.pendingOrder.setOnClickListener {
            val i = Intent(this, PendingOrderActivity::class.java)
            startActivity(i)
        }

        binding.logout.setOnClickListener {
            Firebase.auth.signOut()
            Toast.makeText(this , "LogOut Successfully" , Toast.LENGTH_SHORT).show()
            val i = Intent(this , LoginActivity2::class.java)
            startActivity(i)
            finish()
        }


        pendingOrder()
        completedOrder()
        totalEarning()
    }

    private fun totalEarning() {
        var listOfTotalEarning = mutableListOf<Int>()

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
              for (orderSnapshot in snapshot.children){
                  val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                  completeOrder?.totalPrice?.replace("₹" , "" )?.toIntOrNull()?.let {
                      i -> listOfTotalEarning.add(i)
                  }
              }
                binding.totalEarnings.text = listOfTotalEarning.sum().toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun completedOrder() {
        val completedOrderReference = database.reference.child("CompletedOrder")
        var completedOrderItemCount = 0
        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completedOrderItemCount = snapshot.childrenCount.toInt()
                binding.completeOrder.text = completedOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun pendingOrder() {
        val pendingOrderReference = database.reference.child("OrderDetails")
        var pendingOrderItemCount = 0
        pendingOrderReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount = snapshot.childrenCount.toInt()
                binding.pendingOrders.text = pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}