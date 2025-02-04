package com.example.eradoaco.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.eradoaco.models.Progress

class ProgressDAO(context: Context) {
    private val dbHelper = Database(context)

    fun saveProgress(progress: Progress) {
        if (getProgress() != null) return
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("MONEY", progress.money)
            put("FK_ID_UPGRADE", progress.upgradeId)
            put("FK_ID_MANAGER", progress.managerId)
            put("FK_ID_ACHIEVEMENTS", progress.achievementsId)
            put("FK_ID_TOOL", progress.toolId)
        }
        db.insert("PROGRESS", null, values)
        db.close()
    }

    fun getProgress(): Progress? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM PROGRESS LIMIT 1", null)
        var progress: Progress? = null

        if (cursor.moveToFirst()) {
            progress = Progress(
                cursor.getInt(cursor.getColumnIndexOrThrow("MONEY")),
                cursor.getInt(cursor.getColumnIndexOrThrow("FK_ID_UPGRADE")),
                cursor.getInt(cursor.getColumnIndexOrThrow("FK_ID_MANAGER")),
                cursor.getInt(cursor.getColumnIndexOrThrow("FK_ID_ACHIEVEMENTS")),
                cursor.getInt(cursor.getColumnIndexOrThrow("FK_ID_TOOL"))
            )
        }
        cursor.close()
        db.close()
        return progress
    }
}

