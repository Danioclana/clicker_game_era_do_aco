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

        btnSaveProgress.setOnClickListener { saveTest() }
        btnLoadProgress.setOnClickListener { loadTest() }
    }

    private fun saveTest() {
        val toolDAO = ToolDAO(this)
        val upgradeDAO = UpgradeDAO(this)
        val achievementDAO = AchievementDAO(this)
        val managerDAO = ManagerDAO(this)

        val tool = Tool(1, ToolName.PREGO, 0)
        val upgrade = Upgrade(1, UpgradeName.PREGOS_DE_FERRO_DE_QUALIDADE, 0)
        val achievement = Achievement(1, AchievementName.PRIMEIRO_PREGO, 0)
        val manager = Manager(1, 0)

        toolDAO.insertTool(tool)
        upgradeDAO.insertUpgrade(upgrade)
        achievementDAO.insertAchievement(achievement)
        managerDAO.insertManager(manager)

        val tools = toolDAO.getTools()
        val upgrades = upgradeDAO.getUpgrades()
        val achievements = achievementDAO.getAchievements()
        val managers = managerDAO.getManagers()

        Log.d("DB_TEST", "Ferramentas: $tools")
        Log.d("DB_TEST", "Upgrades: $upgrades")
        Log.d("DB_TEST", "Conquistas: $achievements")
        Log.d("DB_TEST", "Gerentes: $managers")
    }

    private fun loadTest() {
        val tools = ToolDAO(this).getTools()
        val upgrades = UpgradeDAO(this).getUpgrades()
        val achievements = AchievementDAO(this).getAchievements()
        val managers = ManagerDAO(this).getManagers()

        Log.d("DB_TEST", "Ferramentas: $tools")
        Log.d("DB_TEST", "Upgrades: $upgrades")
        Log.d("DB_TEST", "Conquistas: $achievements")
        Log.d("DB_TEST", "Gerentes: $managers")
    }
}
