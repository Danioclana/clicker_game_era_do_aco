package com.example.eradoaco.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.eradoaco.models.Manager

class ManagerDAO(context: Context) {
    private val dbHelper = Database(context)

    fun insertManager(manager: Manager) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("ID", manager.id)
            put("AMOUNT", manager.amount)
        }
        db.insert("CLASS_MANAGER", null, values)
        db.close()
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
        cursor.close()
        db.close()
        return managers
    }
}
