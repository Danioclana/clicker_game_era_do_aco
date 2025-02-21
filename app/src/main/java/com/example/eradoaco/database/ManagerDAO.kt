package com.example.eradoaco.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.eradoaco.models.Manager

class ManagerDAO(context: Context) {
    private val dbHelper = Database(context)

    fun saveOrUpdateManager(manager: Manager) {
        if (getManagers().any { it.id == manager.id }) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("ID", manager.id)
                put("AMOUNT", manager.amount)
            }

            Log.d("ManagerDAO", "saveOrUpdateManager: $values")

            db.update("CLASS_MANAGER", values, "ID = ${manager.id}", null)
            db.close()
        } else {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("ID", manager.id)
                put("AMOUNT", manager.amount)
            }

            Log.d("ManagerDAO", "saveOrUpdateManager: $values")

            db.insert("CLASS_MANAGER", null, values)
            db.close()
        }
    }

    fun getManagers(): List<Manager> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM CLASS_MANAGER", null)
        val managers = mutableListOf<Manager>()

        while (cursor.moveToNext()) {
            managers.add(
                Manager(
                    cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("AMOUNT"))
                )
            )
        }

        Log.d("ManagerDAO", "getManagers: $managers")

        cursor.close()
        db.close()
        return managers
    }
}
