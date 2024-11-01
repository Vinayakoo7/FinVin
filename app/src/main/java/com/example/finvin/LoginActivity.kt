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
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001 // Request code for Google Sign-In

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Ensure this ID is in strings.xml
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set up Google Sign-In button
        val googleSignInButton: Button = findViewById(R.id.googleSignInButton)
        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        // AWS Cognito Hosted UI Login Button
        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            val loginUri = Uri.parse(
                "https://finvin.auth.ap-south-1.amazoncognito.com/oauth2/authorize" +
                        "?client_id=3e1ifha5vaqoqeeg28orddb3b3" +
                        "&response_type=code" +
                        "&scope=email+openid+phone" +
                        "&redirect_uri=com.example.finvin:/callback"
            )
            startActivity(Intent(Intent.ACTION_VIEW, loginUri))
        }
    }

    // Handle Google Sign-In intent result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                // Use the ID token to log in to AWS Cognito
                authenticateWithCognito(idToken)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "Google Sign-In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun authenticateWithCognito(idToken: String) {
        // Implement AWS Cognito login using the Google ID token
        Log.d("LoginActivity", "Google ID Token: $idToken")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    private fun exchangeCodeForToken(authCode: String) {
        // Implement token exchange logic with AWS Cognito
        Log.d("LoginActivity", "Auth Code: $authCode")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
