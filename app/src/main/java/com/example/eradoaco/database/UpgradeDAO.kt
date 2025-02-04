package com.example.eradoaco.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.eradoaco.models.Upgrade
import com.example.eradoaco.models.UpgradeName

class UpgradeDAO(context: Context) {
    private val dbHelper = Database(context)

    fun insertUpgrade(upgrade: Upgrade) {
        if (getUpgrades().any { it.id == upgrade.id }) return
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("ID", upgrade.id)
            put("NAME", upgrade.name.name)
            put("PROGRESS", upgrade.progress)
        }
        db.insert("CLASS_UPGRADE", null, values)
        db.close()
    }

    fun getUpgrades(): List<Upgrade> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM CLASS_UPGRADE", null)
        val upgrades = mutableListOf<Upgrade>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
            val nameString = cursor.getString(cursor.getColumnIndexOrThrow("NAME"))
            val progress = cursor.getInt(cursor.getColumnIndexOrThrow("PROGRESS"))

            val name = UpgradeName.fromString(nameString) ?: continue
            upgrades.add(Upgrade(id, name, progress))
        }
        cursor.close()
        db.close()
        return upgrades
    }
}
