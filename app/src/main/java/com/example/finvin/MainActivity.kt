package com.example.finvin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var databaseManager: DatabaseManager
    private lateinit var balanceTextView: TextView
    private lateinit var incomeTextView: TextView  // Add this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the databaseManager
        databaseManager = DatabaseManager(this)

        // Initialize the TextViews inside the dashboard
        balanceTextView = findViewById(R.id.card_balance_value)
        incomeTextView = findViewById(R.id.card_income_value)  // Bind the income card's TextView

        // Initialize the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set the custom title layout
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val inflater = layoutInflater
        val customTitleView = inflater.inflate(R.layout.toolbar_title, null)
        toolbar.addView(customTitleView)

        // Initialize DrawerLayout and ActionBarDrawerToggle
        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle navigation drawer item clicks
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_income -> {
                    val intent = Intent(this, IncomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_balance -> {
                    val intent = Intent(this, BalanceActivity::class.java) // Navigate to BalanceActivity
                    startActivity(intent)
                    true
                }
                R.id.nav_home -> true
                R.id.nav_budget -> true
                R.id.nav_expense -> true
                else -> false
            }
        }
    }

    // This will be called every time MainActivity is resumed (e.g., when returning from another activity)
    override fun onResume() {
        super.onResume()
        updateDashboard()  // Refresh the dashboard dynamically when the user returns to this activity
    }

    // Function to update the dashboard after inserting income and retrieving balance
    private fun updateDashboard() {
        val balance = databaseManager.getBalance()
        val income = databaseManager.getIncome()

        // Update the TextViews inside the cards
        balanceTextView.text = "₹$balance"
        incomeTextView.text = "₹$income"
    }

    override fun onDestroy() {
        super.onDestroy()
        // Close the database when the activity is destroyed
        databaseManager.close()
    }
}
