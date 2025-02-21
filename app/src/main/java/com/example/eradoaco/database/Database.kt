package com.example.eradoaco.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE CLASS_MONEY (ID INTEGER PRIMARY KEY, MONEY INTEGER)")
        db.execSQL("CREATE TABLE CLASS_TOOL (ID INTEGER PRIMARY KEY, TEMPOPRODUCAO INTEGER, VALORPROXCOMPRA INTEGER, QUANTIDADE INTEGER, VALOR INTEGER)")
        db.execSQL("CREATE TABLE CLASS_ACHIEVEMENTS (ID INTEGER PRIMARY KEY, NAME TEXT, PROGRESS INTEGER)")
        db.execSQL("CREATE TABLE CLASS_MANAGER (ID INTEGER PRIMARY KEY, AMOUNT INTEGER)")
        db.execSQL("CREATE TABLE CLASS_UPGRADE (ID INTEGER PRIMARY KEY, NAME TEXT, PROGRESS INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS CLASS_MONEY")
        db.execSQL("DROP TABLE IF EXISTS CLASS_TOOL")
        db.execSQL("DROP TABLE IF EXISTS CLASS_ACHIEVEMENTS")
        db.execSQL("DROP TABLE IF EXISTS CLASS_MANAGER")
        db.execSQL("DROP TABLE IF EXISTS CLASS_UPGRADE")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "game.db"
        private const val DATABASE_VERSION = 2
    }
}