package com.awcindia.adminappeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.awcindia.adminappeat.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

open class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            val i = Intent(this, PendingActivity::class.java)
            startActivity(i)
        }

        binding.logout.setOnClickListener {
            Firebase.auth.signOut()
            Toast.makeText(this , "LogOut Successfully" , Toast.LENGTH_SHORT).show()
            val i = Intent(this , LoginActivity2::class.java)
            startActivity(i)
            finish()
        }
    }
}