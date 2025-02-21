package com.example.eradoaco.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.eradoaco.models.Tool
import com.example.eradoaco.models.ToolName

class ToolDAO(context: Context) {
    private val dbHelper = Database(context)

    fun insertTool(tool: Tool) {
        if (getTools().any { it.id == tool.id }) return
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("ID", tool.id)
            put("tempoProducao", tool.tempoProducao)
            put("valorProxCompra", tool.valorProxCompra)
            put("quatidade", tool.quatidade)
        }
        db.insert("CLASS_TOOL", null, values)
        db.close()
    }

    fun getTools(): List<Tool> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM CLASS_TOOL", null)
        val tools = mutableListOf<Tool>()

        while (cursor.moveToNext()) {
            tools.add(
                Tool(
                    cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("tempoProducao")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("valorProxCompra")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("quatidade"))
                )
            )
        }
        cursor.close()
        db.close()
        return tools
    }
}
