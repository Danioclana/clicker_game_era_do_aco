package com.example.eradoaco

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.eradoaco.models.*
import com.example.eradoaco.database.*

class DatabaseTestActivity : AppCompatActivity() {
    private lateinit var progressDAO: ProgressDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_test)

        progressDAO = ProgressDAO(this)

        val btnSaveProgress = findViewById<Button>(R.id.btn_insert)
        val btnLoadProgress = findViewById<Button>(R.id.btn_fetch)

        btnSaveProgress.setOnClickListener { saveTestProgress() }
        btnLoadProgress.setOnClickListener { loadTestProgress() }
    }

    private fun saveTestProgress() {
        val progress = Progress(
            money = 1000,
            upgradeId = 2,
            managerId = 1,
            achievementsId = 3,
            toolId = 5
        )
        progressDAO.saveProgress(progress)
        Log.d("DB_TEST", "Progresso salvo com sucesso: $progress")
    }

    private fun loadTestProgress() {
        val progress = progressDAO.getProgress()
        progress?.let {
            Log.d("DB_TEST", "Progresso carregado - Dinheiro: ${it.money}, Upgrade: ${it.upgradeId}")
        } ?: Log.d("DB_TEST", "Nenhum progresso encontrado!")
    }
}
