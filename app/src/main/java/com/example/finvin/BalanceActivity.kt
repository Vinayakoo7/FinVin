package com.example.finvin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class BalanceActivity : AppCompatActivity() {

    private lateinit var databaseManager: DatabaseManager
    private lateinit var balanceInput: EditText
    private lateinit var balanceTypeInput: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance)

        // Initialize the database manager
        databaseManager = DatabaseManager(this)

        // Find the input fields and button
        balanceInput = findViewById(R.id.balance_input)
        balanceTypeInput = findViewById(R.id.balance_type_input)
        submitButton = findViewById(R.id.submit_balance_button)

        // Handle balance submission
        submitButton.setOnClickListener {
            val balance = balanceInput.text.toString().toDoubleOrNull()
            val balanceType = balanceTypeInput.text.toString()

            if (balance != null && balanceType.isNotEmpty()) {
                // Insert the balance into the database
                databaseManager.addBalance(balance, balanceType)
                finish() // Close the activity and return to the dashboard
            } else {
                // Handle invalid input (e.g., show a toast)
            }
        }
    }
}
