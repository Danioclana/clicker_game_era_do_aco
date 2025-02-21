package com.example.eradoaco.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE USER (NAME TEXT, EMAIL TEXT, FK_ID_PROGRESS INTEGER)")
        db.execSQL("CREATE TABLE PROGRESS (MONEY INTEGER, FK_ID_UPGRADE INTEGER, FK_ID_MANAGER INTEGER, FK_ID_ACHIEVEMENTS INTEGER, FK_ID_TOOL INTEGER)")
        db.execSQL("CREATE TABLE CLASS_TOOL (ID INTEGER PRIMARY KEY, TEMPOPRODUCAO INTEGER, VALORPROXCOMPRA INTEGER, QUANTIDADE INTEGER)")
        db.execSQL("CREATE TABLE CLASS_ACHIEVEMENTS (ID INTEGER PRIMARY KEY, NAME TEXT, PROGRESS INTEGER)")
        db.execSQL("CREATE TABLE CLASS_MANAGER (ID INTEGER PRIMARY KEY, AMOUNT INTEGER)")
        db.execSQL("CREATE TABLE CLASS_UPGRADE (ID INTEGER PRIMARY KEY, NAME TEXT, PROGRESS INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS USER")
        db.execSQL("DROP TABLE IF EXISTS PROGRESS")
        db.execSQL("DROP TABLE IF EXISTS CLASS_TOOL")
        db.execSQL("DROP TABLE IF EXISTS CLASS_ACHIEVEMENTS")
        db.execSQL("DROP TABLE IF EXISTS CLASS_MANAGER")
        db.execSQL("DROP TABLE IF EXISTS CLASS_UPGRADE")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "game.db"
        private const val DATABASE_VERSION = 1
    }
}