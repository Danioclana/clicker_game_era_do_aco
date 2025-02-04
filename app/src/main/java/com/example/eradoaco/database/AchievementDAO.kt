package com.example.eradoaco.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.eradoaco.models.Achievement

class AchievementDAO(context: Context) {
    private val dbHelper = Database(context)

    fun insertAchievement(achievement: Achievement) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("ID", achievement.id)
            put("NAME", achievement.name)
            put("PROGRESS", achievement.progress)
        }
        db.insert("CLASS_ACHIEVEMENTS", null, values)
        db.close()
    }

    fun getAchievements(): List<Achievement> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM CLASS_ACHIEVEMENTS", null)
        val achievements = mutableListOf<Achievement>()

        while (cursor.moveToNext()) {
            achievements.add(
                Achievement(
                    cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NAME")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("PROGRESS"))
                )
            )
        }
        cursor.close()
        db.close()
        return achievements
    }
}