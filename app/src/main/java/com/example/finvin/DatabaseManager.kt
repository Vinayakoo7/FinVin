package com.example.finvin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "finvin.db"
        const val DATABASE_VERSION = 8

        // Table names
        const val TABLE_USER_DATA = "UserData"
        const val TABLE_INCOME = "IncomeTable"
        const val TABLE_BALANCE = "BalanceTable"  // New balance table

        // Columns for UserData
        const val COLUMN_ID = "id"

        // Columns for IncomeTable
        const val COLUMN_INCOME_TYPE = "income_type"
        const val COLUMN_INCOME_AMOUNT = "income"

        // Columns for BalanceTable
        const val COLUMN_BALANCE_TYPE = "balance_type"  // You can define types if needed (e.g., savings, checking)
        const val COLUMN_BALANCE_AMOUNT = "balance"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create UserData table
        val createUserDataTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_USER_DATA (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT
            )
        """
        db.execSQL(createUserDataTable)

        // Create IncomeTable
        val createIncomeTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_INCOME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_INCOME_TYPE TEXT,
                $COLUMN_INCOME_AMOUNT REAL
            )
        """
        db.execSQL(createIncomeTable)

        // Create BalanceTable
        val createBalanceTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_BALANCE (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_BALANCE_TYPE TEXT,
                $COLUMN_BALANCE_AMOUNT REAL
            )
        """
        db.execSQL(createBalanceTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_DATA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INCOME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BALANCE")  // Drop balance table if it exists
        // Create tables again
        onCreate(db)
    }

    // Insert income into the IncomeTable
    fun addIncome(income: Double, incomeType: String) {
        val values = ContentValues().apply {
            put(COLUMN_INCOME_AMOUNT, income)
            put(COLUMN_INCOME_TYPE, incomeType)
        }
        writableDatabase.insert(TABLE_INCOME, null, values)
    }

    // Retrieve the user's income from the IncomeTable
    fun getIncome(): Double {
        var income = 0.0
        val cursor = readableDatabase.query(
            TABLE_INCOME,  // Fetch from the income table
            arrayOf(COLUMN_INCOME_AMOUNT),  // Get the income column
            null, null, null, null, null
        )
        if (cursor.moveToFirst()) {
            income = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_INCOME_AMOUNT))
        }
        cursor.close()
        return income
    }

    // Insert balance into the BalanceTable
    fun addBalance(balance: Double, balanceType: String) {
        val values = ContentValues().apply {
            put(COLUMN_BALANCE_AMOUNT, balance)
            put(COLUMN_BALANCE_TYPE, balanceType)
        }
        writableDatabase.insert(TABLE_BALANCE, null, values)
    }

    // Retrieve the user's balance from the BalanceTable
    fun getBalance(): Double {
        var balance = 0.0
        val cursor = readableDatabase.query(
            TABLE_BALANCE,  // Fetch from the balance table
            arrayOf(COLUMN_BALANCE_AMOUNT),  // Get the balance column
            null, null, null, null, null
        )
        if (cursor.moveToFirst()) {
            balance = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BALANCE_AMOUNT))
        }
        cursor.close()
        return balance
    }

    // Close the database when done
    override fun close() {
        writableDatabase.close()
    }
}
