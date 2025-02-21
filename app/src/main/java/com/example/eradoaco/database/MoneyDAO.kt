package com.example.eradoaco.database

import android.content.Context
import android.util.Log

class MoneyDAO(context: Context) {
    private val dbHelper = Database(context)

    fun saveOrUpdateMoney(money: Int) {
        val db = dbHelper.writableDatabase
        db.execSQL("INSERT OR REPLACE INTO CLASS_MONEY (ID, MONEY) VALUES (1, $money)")
        Log.d ("MoneyDAO", "saveOrUpdateMoney: $money")
        db.close()
    }

    fun getMoney(): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT MONEY FROM CLASS_MONEY", null)

        val money = if (cursor.moveToFirst()) {
            cursor.getInt(0)
        } else {
            0
        }

        Log.d("MoneyDAO", "getMoney: $money")

        cursor.close()
        return money
    }

}
