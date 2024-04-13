package com.awcindia.adminappeat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.awcindia.adminappeat.databinding.ActivityFaceBookLoginBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.NonCancellable.cancel
import java.util.Arrays

class FaceBook_Login : MainActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var callbackmanager: CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_book_login)

        FacebookSdk.sdkInitialize(applicationContext)
        callbackmanager = CallbackManager.Factory.create()
        auth = FirebaseAuth.getInstance()

        LoginManager.getInstance().logInWithReadPermissions(this , listOf("public_profile"))
        LoginManager.getInstance().registerCallback(callbackmanager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken);
                }

                override fun onCancel() {
                    Log.d("Account", "facebook:onCancel")
                    cancel()
                }

                override fun onError(error: FacebookException) {
                    Log.d("Account", "onError$error")
                }
            })

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackmanager.onActivityResult(requestCode, resultCode, data)
    }

    // Fun for facebook login
    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "FaceBook LogIn Successfully", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                 updateUi(user)
                } else {
                    Log.w("Account", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
    // simple Intent
    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    // fun for cancel
    private fun cancel(){
        val i = Intent(this , LoginActivity2::class.java)
        startActivity(i)
    }
}
