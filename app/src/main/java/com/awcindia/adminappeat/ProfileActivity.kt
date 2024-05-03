package com.awcindia.adminappeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.awcindia.adminappeat.Modals.UserModal
import com.awcindia.adminappeat.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("user")


        binding.back.setOnClickListener {
            finish()
        }

        binding.save.setOnClickListener {
            updateUserData()
            binding.name.isEnabled = false
            binding.restaurants.isEnabled = false
            binding.address.isEnabled = false
            binding.email.isEnabled = false
            binding.phoneNum.isEnabled = false
            binding.save.isEnabled = false
        }

        binding.name.isEnabled = false
        binding.restaurants.isEnabled = false
        binding.address.isEnabled = false
        binding.email.isEnabled = false
        binding.phoneNum.isEnabled = false
        binding.save.isEnabled = false

        var isEnable = false

        binding.edit.setOnClickListener {
            isEnable = !isEnable
            binding.name.isEnabled = isEnable
            binding.restaurants.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.phoneNum.isEnabled = isEnable
            binding.save.isEnabled = isEnable

            if (isEnable){
                binding.name.requestFocus()
            }
        }

        retrieveUserData()
    }

    private fun updateUserData() {

        val updateName = binding.name.text.toString()
        val updateRestaurants = binding.restaurants.text.toString()
        val updateEmail = binding.email.text.toString()
        val updateAddress = binding.address.text.toString()
        val updatePhone = binding.phoneNum.text.toString()

        if (updateName.isBlank() || updateEmail.isBlank() || updateAddress.isBlank() || updateRestaurants.isBlank() || updatePhone.isBlank()) {
            Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_SHORT).show()
        } else {
            val userData =
                UserModal(updateName, updateRestaurants, updateEmail, updateAddress, updatePhone)
            adminReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                auth.currentUser?.updateEmail(updateEmail)

            }.addOnFailureListener {
                Toast.makeText(this, "Some Thing Wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun retrieveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = adminReference.child(currentUserUid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val ownerName = snapshot.child("name").getValue()
                        val restaurants = snapshot.child("nameOfRestaurant").getValue()
                        val email = snapshot.child("email").getValue()
                        val address = snapshot.child("address").getValue()
                        val phone = snapshot.child("phone").getValue()

                        setDataToTextView(ownerName, restaurants,email, address, phone)

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private fun setDataToTextView(ownerName: Any?, restaurants : Any?,email: Any?, address: Any?, phone: Any?) {
        binding.name.setText(ownerName.toString())
        binding.restaurants.setText(restaurants.toString())
        binding.email.setText(email.toString())
        binding.address.setText(address.toString())
        binding.phoneNum.setText(phone.toString())
    }
}