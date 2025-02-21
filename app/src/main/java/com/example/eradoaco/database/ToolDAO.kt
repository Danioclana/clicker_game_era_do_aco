package com.example.eradoaco.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.eradoaco.models.Tool


class ToolDAO(context: Context) {
    private val dbHelper = Database(context)

    fun saveOrUpdateTool(tool: Tool) {
        if (getTools().any { it.id == tool.id }) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("ID", tool.id)
                put("TEMPOPRODUCAO", tool.tempoProducao)
                put("VALORPROXCOMPRA", tool.valorProxCompra)
                put("QUANTIDADE", tool.quantidade)
            }

            Log.d("ToolDAO", "saveOrUpdateTool: $values")

            db.update("CLASS_TOOL", values, "ID = ${tool.id}", null)
            db.close()
        } else {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("ID", tool.id)
                put("TEMPOPRODUCAO", tool.tempoProducao)
                put("VALORPROXCOMPRA", tool.valorProxCompra)
                put("QUANTIDADE", tool.quantidade)
            }

            Log.d("ToolDAO", "saveOrUpdateTool: $values")

            db.insert("CLASS_TOOL", null, values)
            db.close()
        }
    }

    fun getTools(): List<Tool> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM CLASS_TOOL", null)
        val tools = mutableListOf<Tool>()

        while (cursor.moveToNext()) {
            tools.add(
                Tool(
                    cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("TEMPOPRODUCAO")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("VALORPROXCOMPRA")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("QUANTIDADE"))
                )
            )
        }

        Log.d("ToolDAO", "getTools: $tools")

        cursor.close()
        db.close()
        return tools
    }
}
