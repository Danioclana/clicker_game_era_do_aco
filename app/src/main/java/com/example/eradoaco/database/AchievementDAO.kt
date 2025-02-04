package com.example.eradoaco.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.eradoaco.models.Achievement
import com.example.eradoaco.models.AchievementName

class AchievementDAO(context: Context) {
    private val dbHelper = Database(context)

    fun insertAchievement(achievement: Achievement) {
        if (getAchievements().any { it.id == achievement.id }) return
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("ID", achievement.id)
            put("NAME", achievement.name.name)
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
            val achievementNameString = cursor.getString(cursor.getColumnIndexOrThrow("NAME"))
            val achievementName = AchievementName.fromString(achievementNameString) ?: AchievementName.PRIMEIRO_PREGO

            achievements.add(
                Achievement(
                    cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                    achievementName,
                    cursor.getInt(cursor.getColumnIndexOrThrow("PROGRESS"))
                )
            )
        }
        cursor.close()
        db.close()
        return achievements
    }
}
