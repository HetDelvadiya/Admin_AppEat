package com.awcindia.adminappeat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.awcindia.adminappeat.databinding.ActivityLogin2Binding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

open class LoginActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityLogin2Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var emailuser: String
    private lateinit var passwordEditText: String
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)



        auth = FirebaseAuth.getInstance()




        // Google SignIn method
        googleSignInClient = GoogleSignIn.getClient(
            this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                getString(com.firebase.ui.auth.R.string.default_web_client_id)).requestEmail().build())

        // Login process
        binding.login.setOnClickListener {
            emailuser = binding.email.text.toString().trim()
            passwordEditText = binding.password.text.toString().trim()

            if (emailuser.isBlank() || passwordEditText.isBlank()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                signIn(emailuser, passwordEditText)
            }
        }

        binding.google.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }

        binding.facebook.setOnClickListener {
            val i = Intent(this , FaceBook_Login::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(i)
            finish()
        }



        binding.signupText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }



    // Google login fun
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount? = task.result
                account?.let {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authtask ->
                        if (authtask.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Successfully SignIn with Google",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUi(authtask.result?.user)
                        } else {
                            Toast.makeText(this, "Google SignIn Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } ?: run {
                    Toast.makeText(this, "Google SignIn Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // For LogIn
    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUi(user)
                } else {
                    Toast.makeText(
                        baseContext, "Enter valid email and password ",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("Account", "Failed", task.exception)
                }
            }
    }


    // For user is already logged In or not
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUi(currentUser)
        }
    }

    // simple Intent
    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}




