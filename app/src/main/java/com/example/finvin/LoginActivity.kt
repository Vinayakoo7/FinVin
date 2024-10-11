package com.example.finvin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001 // Request code for Google Sign-In

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize buttons
        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            // Redirect to Cognito Hosted UI for login
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://finvin.auth.ap-south-1.amazoncognito.com/oauth2/authorize?client_id=3e1ifha5vaqoqeeg28orddb3b3&response_type=code&scope=email+openid+phone&redirect_uri=com.example.finvin:/callback"))
            startActivity(intent)
        }

        val registerButton: Button = findViewById(R.id.registerButton)
        registerButton.setOnClickListener {
            // Redirect to Cognito Hosted UI for registration
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://finvin.auth.ap-south-1.amazoncognito.com/oauth2/authorize?client_id=3e1ifha5vaqoqeeg28orddb3b3&response_type=code&scope=email+openid+phone&redirect_uri=com.example.finvin:/callback"))
            startActivity(intent)
        }

        // Configure Google Sign-In options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1038272972481-vjvddknf3vtiavfeldl3gr3abdgbvua2.apps.googleusercontent.com")  // Replace with your Google Client ID
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Google Sign-In button
        val googleSignInButton: Button = findViewById(R.id.googleSignInButton)
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
            Log.w("LoginActivity", "Google sign in failed", e)
            Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_LONG).show()
        }
    }

    private fun authenticateWithCognito(idToken: String) {
        // Replace this block with your Cognito authentication logic using the idToken
        Toast.makeText(this, "Google Sign-In Success!", Toast.LENGTH_LONG).show()
        // Proceed to the next activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Handle Cognito Hosted UI callback
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val uri: Uri? = intent.data
        if (uri != null && uri.toString().startsWith("com.example.finvin:/callback")) {
            val code = uri.getQueryParameter("code")
            if (code != null) {
                // Use the authorization code to request tokens from Cognito
                exchangeCodeForToken(code)
            }
        }
    }


    private fun exchangeCodeForToken(code: String) {
        // You need to make a POST request to the Cognito /token endpoint to exchange the code for tokens.
        // Example using OkHttp or Retrofit:
        // POST to: https://finvin.auth.ap-south-1.amazoncognito.com/oauth2/token
        // with parameters: grant_type=authorization_code, client_id, code, redirect_uri
        Toast.makeText(this, "Received authorization code: $code", Toast.LENGTH_LONG).show()
        // Add your logic here to exchange the code for tokens and handle success/failure
    }
}
