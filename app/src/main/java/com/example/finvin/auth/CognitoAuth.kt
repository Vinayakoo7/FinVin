package com.example.finvin.auth

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.exceptions.CognitoIdentityProviderException
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult

class CognitoAuth(private val context: Context) {

    // Initialize CognitoUserPool
    private val userPool: CognitoUserPool = CognitoUserPool(
        context,
        "ap-south-1_3FlaOV738",     // Your correct Cognito User Pool ID
        "3e1ifha5vaqoqeeg28orddb3b3",    // Replace with the correct Cognito App Client ID
        "ap-south-1"           // Your AWS region (make sure it's correct)
    )

    // Sign-Up function for registering new users
    fun signUp(username: String, password: String, userAttributes: CognitoUserAttributes, onSuccess: (result: String) -> Unit, onFailure: (exception: Exception) -> Unit) {
        userPool.signUpInBackground(username, password, userAttributes, null, object : SignUpHandler {
            override fun onSuccess(cognitoUser: CognitoUser, signUpResult: SignUpResult) {
                if (signUpResult.userConfirmed) {
                    // User is automatically confirmed
                    onSuccess("User confirmed")
                } else {
                    // Confirmation required (via email)
                    onSuccess("User needs to confirm email")
                }
            }

            override fun onFailure(exception: Exception) {
                onFailure(exception)
            }
        })
    }

    // Username/Password sign-in
    fun signIn(username: String, password: String, onSuccess: (session: CognitoUserSession) -> Unit, onFailure: (exception: Exception) -> Unit) {
        val authenticationHandler = object : AuthenticationHandler {
            override fun onSuccess(userSession: CognitoUserSession, cognitoDevice: CognitoDevice?) {
                onSuccess(userSession)
            }

            override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation?, userId: String?) {
                val authDetails = AuthenticationDetails(username, password, mapOf())
                authenticationContinuation?.setAuthenticationDetails(authDetails)
                authenticationContinuation?.continueTask()
            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                // Handle MFA if needed
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                // Handle other challenges if necessary
            }

            override fun onFailure(exception: Exception?) {
                if (exception != null) {
                    onFailure(exception)
                }
            }
        }

        val user = userPool.getUser(username)
        user.getSessionInBackground(authenticationHandler)
    }

    // Google Sign-In to Cognito
    fun signInWithGoogle(idToken: String, onSuccess: (session: CognitoUserSession) -> Unit, onFailure: (exception: Exception) -> Unit) {
        val authenticationHandler = object : AuthenticationHandler {
            override fun onSuccess(userSession: CognitoUserSession, cognitoDevice: CognitoDevice?) {
                onSuccess(userSession)
            }

            override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation?, userId: String?) {
                val authDetails = AuthenticationDetails(null, mapOf("id_token" to idToken), mapOf())
                authenticationContinuation?.setAuthenticationDetails(authDetails)
                authenticationContinuation?.continueTask()
            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                // Handle MFA if needed
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                // Handle other challenges if needed
            }

            override fun onFailure(exception: Exception?) {
                if (exception != null) {
                    onFailure(exception)
                }
            }
        }

        val user = userPool.getUser() // Anonymous user for Google login
        user.getSessionInBackground(authenticationHandler)
    }
}
