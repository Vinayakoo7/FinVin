package com.example.finvin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class IncomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income)

        val incomeInput = findViewById<EditText>(R.id.income_input)
        val monthlyButton = findViewById<RadioButton>(R.id.radio_monthly)
        val weeklyButton = findViewById<RadioButton>(R.id.radio_weekly)
        val submitButton = findViewById<Button>(R.id.submit_income_button)

        // Initialize the DatabaseManager
        val databaseManager = DatabaseManager(this)

        submitButton.setOnClickListener {
            val incomeValue = incomeInput.text.toString()
            val incomeType = if (monthlyButton.isChecked) "Monthly" else "Weekly"

            if (incomeValue.isNotEmpty()) {
                // Call the addIncome method from DatabaseManager
                databaseManager.addIncome(incomeValue.toDouble(), incomeType)
                Toast.makeText(this, "Income added successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity
            } else {
                Toast.makeText(this, "Please enter an income amount", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
