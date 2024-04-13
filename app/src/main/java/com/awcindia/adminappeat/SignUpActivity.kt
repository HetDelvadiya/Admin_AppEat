package com.awcindia.adminappeat



import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.awcindia.adminappeat.Modals.UserModal
import com.awcindia.adminappeat.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var database: DatabaseReference
    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = Firebase.auth
        database = Firebase.database.reference

        val locationList = arrayOf("Rajkot", "Ahmedabad", "Surat", "Vadodara", "Junagadh")
        val adapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, locationList)
        val autoCompleteTextView = binding.LISTOFLOCATION
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.threshold = 0

        binding.signup.setOnClickListener {

            userName = binding.name.text.toString().trim()
            nameOfRestaurant = binding.nameOfRestaurant.text.toString().trim()
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (userName.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please Fill All Details", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }



        }

        binding.loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity2::class.java)
            startActivity(intent)

        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this , "Account Creation Failed" , Toast.LENGTH_SHORT).show()
                Log.d("Account" ,"createAccount : Failure" , task.exception)
            }
        }
    }

    private fun saveUserData() {

        userName = binding.name.text.toString().trim()
        nameOfRestaurant = binding.nameOfRestaurant.text.toString().trim()
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()

        val user =UserModal(userName , nameOfRestaurant , email , password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }
}