package com.awcindia.adminappeat



import android.R
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.awcindia.adminappeat.Modals.UserModal
import com.awcindia.adminappeat.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
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
    private lateinit var location : String
    lateinit var binding: ActivitySignUpBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = Firebase.auth
        database = Firebase.database.reference


        googleSignInClient = GoogleSignIn.getClient(
            this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                getString(com.firebase.ui.auth.R.string.default_web_client_id)).requestEmail().build())


        // google sign up
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
            location = binding.LISTOFLOCATION.text.toString().trim()

            if (userName.isBlank() && nameOfRestaurant.isBlank() && email.isBlank() && password.isBlank() && location.isBlank() ) {
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
        location = binding.LISTOFLOCATION.text.toString().trim()

        val user = UserModal(userName , nameOfRestaurant , email ,  location)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUi(currentUser)
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}