package com.example.eradoaco.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.eradoaco.GameActivity
import com.example.eradoaco.models.Progress

class ProgressDAO(context: Context) {
    private val dbHelper = Database(context)

    fun saveOrUpdateProgress(progress: Progress) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("MONEY", progress.money)
            put("FK_ID_UPGRADE", progress.upgradeId)
            put("FK_ID_MANAGER", progress.managerId)
            put("FK_ID_ACHIEVEMENTS", progress.achievementsId)
            put("FK_ID_TOOL", progress.toolId)
        }

        if (getProgress() == null) {
            db.insert("PROGRESS", null, values)
        } else {
            db.update("PROGRESS", values, null, null)
        }
    }

    fun getProgress(): Progress? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM PROGRESS LIMIT 1", null)

        var progress: Progress? = null
        cursor.use {
            if (it.moveToFirst()) {
                progress = Progress(
                    it.getInt(it.getColumnIndexOrThrow("MONEY")),
                    it.getInt(it.getColumnIndexOrThrow("FK_ID_UPGRADE")),
                    it.getInt(it.getColumnIndexOrThrow("FK_ID_MANAGER")),
                    it.getInt(it.getColumnIndexOrThrow("FK_ID_ACHIEVEMENTS")),
                    it.getInt(it.getColumnIndexOrThrow("FK_ID_TOOL"))
                )
            }
        }

        return progress
    }
}
