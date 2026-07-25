package com.example.android_kyc_assignment.data.database
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.android_kyc_assignment.domain.model.AccountType
import com.example.android_kyc_assignment.domain.model.Customer

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "kyc_database.db"
        private const val DATABASE_VERSION = 3  // Increment version for new column
        private const val TABLE_CUSTOMERS = "customers"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_AVATAR = "avatar"
        private const val COLUMN_ACCOUNT_NUMBER = "account_number"
        private const val COLUMN_BALANCE = "balance"
        private const val COLUMN_CURRENCY = "currency"
        private const val COLUMN_IFSC_CODE = "ifsc_code"
        private const val COLUMN_IS_KYC_VERIFIED = "is_kyc_verified"
        private const val COLUMN_SELFIE_PATH = "selfie_path"
        private const val COLUMN_ACCOUNT_TYPE = "account_type"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_CUSTOMERS (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_NAME TEXT,
                $COLUMN_AVATAR TEXT,
                $COLUMN_ACCOUNT_NUMBER TEXT,
                $COLUMN_BALANCE REAL,
                $COLUMN_CURRENCY TEXT,
                $COLUMN_IFSC_CODE TEXT,
                $COLUMN_IS_KYC_VERIFIED INTEGER DEFAULT 0,
                $COLUMN_SELFIE_PATH TEXT,
                $COLUMN_ACCOUNT_TYPE TEXT DEFAULT 'UNKNOWN'
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {
            // Add account_type column
            db.execSQL("ALTER TABLE $TABLE_CUSTOMERS ADD COLUMN $COLUMN_ACCOUNT_TYPE TEXT DEFAULT 'UNKNOWN'")
        }
    }

    fun insertCustomers(customers: List<Customer>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            // Clear existing data
            db.delete(TABLE_CUSTOMERS, null, null)

            for (customer in customers) {
                val values = ContentValues().apply {
                    put(COLUMN_ID, customer.id)
                    put(COLUMN_NAME, customer.name)
                    put(COLUMN_AVATAR, customer.avatar)
                    put(COLUMN_ACCOUNT_NUMBER, customer.accountNumber)
                    put(COLUMN_BALANCE, customer.balance)
                    put(COLUMN_CURRENCY, customer.currency)
                    put(COLUMN_IFSC_CODE, customer.ifscCode)
                    put(COLUMN_IS_KYC_VERIFIED, if (customer.isKycVerified) 1 else 0)
                    put(COLUMN_SELFIE_PATH, customer.selfiePath)
                    put(COLUMN_ACCOUNT_TYPE, customer.accountType.name)
                }
                db.insert(TABLE_CUSTOMERS, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun getAllCustomers(): List<Customer> {
        val customers = mutableListOf<Customer>()
        val db = readableDatabase
        val cursor = db.query(TABLE_CUSTOMERS, null, null, null, null, null, null)

        cursor.use {
            val idIndex = it.getColumnIndex(COLUMN_ID)
            val nameIndex = it.getColumnIndex(COLUMN_NAME)
            val avatarIndex = it.getColumnIndex(COLUMN_AVATAR)
            val accountIndex = it.getColumnIndex(COLUMN_ACCOUNT_NUMBER)
            val balanceIndex = it.getColumnIndex(COLUMN_BALANCE)
            val currencyIndex = it.getColumnIndex(COLUMN_CURRENCY)
            val ifscIndex = it.getColumnIndex(COLUMN_IFSC_CODE)
            val kycIndex = it.getColumnIndex(COLUMN_IS_KYC_VERIFIED)
            val selfieIndex = it.getColumnIndex(COLUMN_SELFIE_PATH)
            val accountTypeIndex = it.getColumnIndex(COLUMN_ACCOUNT_TYPE)

            while (it.moveToNext()) {
                val accountTypeStr = if (accountTypeIndex >= 0) {
                    it.getString(accountTypeIndex) ?: "UNKNOWN"
                } else {
                    "UNKNOWN"
                }
                val accountType = try {
                    AccountType.valueOf(accountTypeStr)
                } catch (e: IllegalArgumentException) {
                    AccountType.UNKNOWN
                }

                val customer = Customer(
                    id = it.getInt(idIndex),
                    name = it.getString(nameIndex),
                    avatar = it.getString(avatarIndex),
                    accountNumber = it.getString(accountIndex),
                    balance = it.getDouble(balanceIndex),
                    currency = it.getString(currencyIndex),
                    ifscCode = it.getString(ifscIndex),
                    isKycVerified = it.getInt(kycIndex) == 1,
                    selfiePath = it.getString(selfieIndex),
                    accountType = accountType
                )
                customers.add(customer)
            }
        }
        db.close()
        return customers
    }

    fun searchCustomers(query: String): List<Customer> {
        val customers = mutableListOf<Customer>()
        val db = readableDatabase
        val selection = "$COLUMN_NAME LIKE ? OR $COLUMN_ACCOUNT_NUMBER LIKE ?"
        val selectionArgs = arrayOf("%$query%", "%$query%")
        val cursor = db.query(TABLE_CUSTOMERS, null, selection, selectionArgs, null, null, null)

        cursor.use {
            val idIndex = it.getColumnIndex(COLUMN_ID)
            val nameIndex = it.getColumnIndex(COLUMN_NAME)
            val avatarIndex = it.getColumnIndex(COLUMN_AVATAR)
            val accountIndex = it.getColumnIndex(COLUMN_ACCOUNT_NUMBER)
            val balanceIndex = it.getColumnIndex(COLUMN_BALANCE)
            val currencyIndex = it.getColumnIndex(COLUMN_CURRENCY)
            val ifscIndex = it.getColumnIndex(COLUMN_IFSC_CODE)
            val kycIndex = it.getColumnIndex(COLUMN_IS_KYC_VERIFIED)
            val selfieIndex = it.getColumnIndex(COLUMN_SELFIE_PATH)
            val accountTypeIndex = it.getColumnIndex(COLUMN_ACCOUNT_TYPE)

            while (it.moveToNext()) {
                val accountTypeStr = if (accountTypeIndex >= 0) {
                    it.getString(accountTypeIndex) ?: "UNKNOWN"
                } else {
                    "UNKNOWN"
                }
                val accountType = try {
                    AccountType.valueOf(accountTypeStr)
                } catch (e: IllegalArgumentException) {
                    AccountType.UNKNOWN
                }

                val customer = Customer(
                    id = it.getInt(idIndex),
                    name = it.getString(nameIndex),
                    avatar = it.getString(avatarIndex),
                    accountNumber = it.getString(accountIndex),
                    balance = it.getDouble(balanceIndex),
                    currency = it.getString(currencyIndex),
                    ifscCode = it.getString(ifscIndex),
                    isKycVerified = it.getInt(kycIndex) == 1,
                    selfiePath = it.getString(selfieIndex),
                    accountType = accountType
                )
                customers.add(customer)
            }
        }
        db.close()
        return customers
    }

    fun updateCustomer(customer: Customer) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, customer.name)
            put(COLUMN_AVATAR, customer.avatar)
            put(COLUMN_ACCOUNT_NUMBER, customer.accountNumber)
            put(COLUMN_BALANCE, customer.balance)
            put(COLUMN_CURRENCY, customer.currency)
            put(COLUMN_IFSC_CODE, customer.ifscCode)
            put(COLUMN_IS_KYC_VERIFIED, if (customer.isKycVerified) 1 else 0)
            put(COLUMN_SELFIE_PATH, customer.selfiePath)
            put(COLUMN_ACCOUNT_TYPE, customer.accountType.name)
        }
        val rowsUpdated = db.update(TABLE_CUSTOMERS, values, "$COLUMN_ID = ?", arrayOf(customer.id.toString()))

        // If no rows updated, insert the customer
        if (rowsUpdated == 0) {
            db.insert(TABLE_CUSTOMERS, null, values)
        }
        db.close()
    }

    fun getCustomerById(id: Int): Customer? {
        val db = readableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.query(TABLE_CUSTOMERS, null, selection, selectionArgs, null, null, null)

        var customer: Customer? = null
        cursor.use {
            if (it.moveToFirst()) {
                val idIndex = it.getColumnIndex(COLUMN_ID)
                val nameIndex = it.getColumnIndex(COLUMN_NAME)
                val avatarIndex = it.getColumnIndex(COLUMN_AVATAR)
                val accountIndex = it.getColumnIndex(COLUMN_ACCOUNT_NUMBER)
                val balanceIndex = it.getColumnIndex(COLUMN_BALANCE)
                val currencyIndex = it.getColumnIndex(COLUMN_CURRENCY)
                val ifscIndex = it.getColumnIndex(COLUMN_IFSC_CODE)
                val kycIndex = it.getColumnIndex(COLUMN_IS_KYC_VERIFIED)
                val selfieIndex = it.getColumnIndex(COLUMN_SELFIE_PATH)
                val accountTypeIndex = it.getColumnIndex(COLUMN_ACCOUNT_TYPE)

                val accountTypeStr = if (accountTypeIndex >= 0) {
                    it.getString(accountTypeIndex) ?: "UNKNOWN"
                } else {
                    "UNKNOWN"
                }
                val accountType = try {
                    AccountType.valueOf(accountTypeStr)
                } catch (e: IllegalArgumentException) {
                    AccountType.UNKNOWN
                }

                customer = Customer(
                    id = it.getInt(idIndex),
                    name = it.getString(nameIndex),
                    avatar = it.getString(avatarIndex),
                    accountNumber = it.getString(accountIndex),
                    balance = it.getDouble(balanceIndex),
                    currency = it.getString(currencyIndex),
                    ifscCode = it.getString(ifscIndex),
                    isKycVerified = it.getInt(kycIndex) == 1,
                    selfiePath = it.getString(selfieIndex),
                    accountType = accountType
                )
            }
        }
        db.close()
        return customer
    }
}