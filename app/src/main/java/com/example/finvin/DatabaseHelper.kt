package com.example.finvin

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "finvin.db"
        const val DATABASE_VERSION = 2

        // Table names
        const val TABLE_USER_DATA = "UserData"
        const val TABLE_EXPENSES = "Expenses"
        const val TABLE_BUDGETS = "Budgets"
        const val TABLE_SUBSCRIPTIONS = "Subscriptions"
        const val TABLE_INCOME = "IncomeTable"  // Add IncomeTable

        // Common Columns
        const val COLUMN_ID = "id"

        // UserData table columns (for single user)
        const val COLUMN_BALANCE = "balance"
        const val COLUMN_INCOME = "income"

        // Income table columns
        const val COLUMN_INCOME_TYPE = "income_type"
        const val COLUMN_INCOME_AMOUNT = "income"

        // Expenses table columns
        const val COLUMN_EXPENSE_AMOUNT = "amount"
        const val COLUMN_EXPENSE_CATEGORY = "category"
        const val COLUMN_EXPENSE_DESCRIPTION = "description"
        const val COLUMN_EXPENSE_DATE = "date"

        // Budgets table columns
        const val COLUMN_BUDGET_TYPE = "budget_type"
        const val COLUMN_BUDGET_AMOUNT = "budget_amount"
        const val COLUMN_BUDGET_START_DATE = "start_date"
        const val COLUMN_BUDGET_END_DATE = "end_date"

        // Subscriptions table columns
        const val COLUMN_SUBSCRIPTION_NAME = "subscription_name"
        const val COLUMN_SUBSCRIPTION_AMOUNT = "amount"
        const val COLUMN_NEXT_DUE_DATE = "next_due_date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create UserData Table (for single user)
        val createUserDataTable = """
            CREATE TABLE $TABLE_USER_DATA (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_BALANCE REAL,
                $COLUMN_INCOME REAL
            );
        """.trimIndent()

        // Create Expenses Table
        val createExpensesTable = """
            CREATE TABLE $TABLE_EXPENSES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EXPENSE_AMOUNT REAL,
                $COLUMN_EXPENSE_CATEGORY TEXT,
                $COLUMN_EXPENSE_DESCRIPTION TEXT,
                $COLUMN_EXPENSE_DATE TEXT
            );
        """.trimIndent()

        // Create Budgets Table
        val createBudgetsTable = """
            CREATE TABLE $TABLE_BUDGETS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_BUDGET_TYPE TEXT,
                $COLUMN_BUDGET_AMOUNT REAL,
                $COLUMN_BUDGET_START_DATE TEXT,
                $COLUMN_BUDGET_END_DATE TEXT
            );
        """.trimIndent()

        // Create Subscriptions Table
        val createSubscriptionsTable = """
            CREATE TABLE $TABLE_SUBSCRIPTIONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SUBSCRIPTION_NAME TEXT,
                $COLUMN_SUBSCRIPTION_AMOUNT REAL,
                $COLUMN_NEXT_DUE_DATE TEXT
            );
        """.trimIndent()

        // Create Income Table
        val createIncomeTable = """
            CREATE TABLE $TABLE_INCOME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_INCOME_TYPE TEXT,
                $COLUMN_INCOME_AMOUNT REAL
            );
        """.trimIndent()

        // Execute all table creation queries
        db.execSQL(createUserDataTable)
        db.execSQL(createExpensesTable)
        db.execSQL(createBudgetsTable)
        db.execSQL(createSubscriptionsTable)
        db.execSQL(createIncomeTable)  // Execute the query to create IncomeTable
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_DATA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXPENSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BUDGETS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SUBSCRIPTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INCOME")  // Drop the IncomeTable if it exists
        onCreate(db)
    }
}
