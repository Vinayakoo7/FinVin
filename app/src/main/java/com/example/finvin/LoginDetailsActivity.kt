package com.example.finvin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finvin.auth.CognitoAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.SignInButton

class LoginDetailsActivity : AppCompatActivity() {

    private lateinit var cognitoAuth: CognitoAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001 // Request code for Google Sign-In

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_details)

        cognitoAuth = CognitoAuth(this)

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val googleSignInButton: SignInButton = findViewById(R.id.googleSignInButton)

        // Handle Login Button Click
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                cognitoAuth.signIn(username, password, { session ->
                    runOnUiThread {
                        Toast.makeText(this, "Login Success!", Toast.LENGTH_LONG).show()
                        // Proceed to next activity
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }, { exception ->
                    runOnUiThread {
                        Toast.makeText(this, "Login Failed: ${exception.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_LONG).show()
            }
        }

        // Configure Google Sign-In options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1038272972481-vjvddknf3vtiavfeldl3gr3abdgbvua2.apps.googleusercontent.com")  // Replace with your Google Client ID
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Handle Google Sign-In Button Click
        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    // Google Sign-In logic
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                val idToken = account.idToken
                if (idToken != null) {
                    authenticateWithCognito(idToken)
                }
            }
        } catch (e: ApiException) {
            Log.w("LoginDetailsActivity", "Google sign in failed", e)
            Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_LONG).show()
        }
    }

    private fun authenticateWithCognito(idToken: String) {
        cognitoAuth.signInWithGoogle(idToken, { session ->
            runOnUiThread {
                Toast.makeText(this, "Google Sign-In Success!", Toast.LENGTH_LONG).show()
                // Proceed to the next activity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, { exception ->
            runOnUiThread {
                Toast.makeText(this, "Google Sign-In Failed: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
