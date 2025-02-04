package com.example.eradoaco.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.eradoaco.models.User

class UserDAO(context: Context) {
    private val dbHelper = Database(context)

    fun insertUser(user: User) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("NAME", user.name)
            put("EMAIL", user.email)
            put("FK_ID_PROGRESS", user.progressId)
        }
        db.insert("USER", null, values)
        db.close()
    }

    fun getUser(): User? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM USER LIMIT 1", null)
        var user: User? = null

        if (cursor.moveToFirst()) {
            user = User(
                cursor.getString(cursor.getColumnIndexOrThrow("NAME")),
                cursor.getString(cursor.getColumnIndexOrThrow("EMAIL")),
                cursor.getInt(cursor.getColumnIndexOrThrow("FK_ID_PROGRESS"))
            )
        }
        cursor.close()
        db.close()
        return user
    }
}
