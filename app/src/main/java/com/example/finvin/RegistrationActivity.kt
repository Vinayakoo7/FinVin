package com.example.finvin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.example.finvin.auth.CognitoAuth

class RegistrationActivity : AppCompatActivity() {

    private lateinit var cognitoAuth: CognitoAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        cognitoAuth = CognitoAuth(this)

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val registerButton: Button = findViewById(R.id.registerButton)

        // Handle Register Button Click
        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val email = emailEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                val attributes = CognitoUserAttributes()
                attributes.addAttribute("email", email)

                cognitoAuth.signUp(username, password, attributes, { confirmation ->
                    runOnUiThread {
                        Toast.makeText(this, "Registration Successful! Confirmation sent to your email.", Toast.LENGTH_LONG).show()
                        finish()  // Return to LoginActivity after registration
                    }
                }, { exception ->
                    runOnUiThread {
                        Toast.makeText(this, "Registration Failed: ${exception.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()
            }
        }
    }
}
