package com.awcindia.adminappeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.awcindia.adminappeat.Adapters.MenuItemAdapter
import com.awcindia.adminappeat.Modals.AllMenu
import com.awcindia.adminappeat.databinding.ActivityAllMenuBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.log

class All_Menu_Activity : AppCompatActivity() {

    private lateinit var binding : ActivityAllMenuBinding

    private lateinit var databaseReference : DatabaseReference
    private lateinit var database : FirebaseDatabase
    private var menuItems : ArrayList<AllMenu> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }


        databaseReference = FirebaseDatabase.getInstance().reference
        retrieveMenuItem()




    }

    private fun retrieveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")


        foodRef.addListenerForSingleValueEvent( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()


                for (foodsnapshot in snapshot.children){
                    val menuItem = foodsnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }



            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError" , "Error : ${error.message}")
            }

        })
    }

    private fun setAdapter() {
        val adapter = MenuItemAdapter(menuItems , this@All_Menu_Activity , databaseReference)
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cartRecyclerView.adapter = adapter

    }
}