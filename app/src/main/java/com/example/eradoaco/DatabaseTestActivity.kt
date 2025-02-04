package com.example.eradoaco

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.eradoaco.models.*
import com.example.eradoaco.database.*

class DatabaseTestActivity : AppCompatActivity() {
    private lateinit var userDAO: UserDAO
    private lateinit var progressDAO: ProgressDAO
    private lateinit var toolDAO: ToolDAO
    private lateinit var achievementDAO: AchievementDAO
    private lateinit var managerDAO: ManagerDAO
    private lateinit var upgradeDAO: UpgradeDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_test)

        // Inicializando DAOs
        userDAO = UserDAO(this)
        progressDAO = ProgressDAO(this)
        toolDAO = ToolDAO(this)
        achievementDAO = AchievementDAO(this)
        managerDAO = ManagerDAO(this)
        upgradeDAO = UpgradeDAO(this)

        val btnInsert = findViewById<Button>(R.id.btn_insert)
        val btnFetch = findViewById<Button>(R.id.btn_fetch)

        btnInsert.setOnClickListener { insertTestData() }
        btnFetch.setOnClickListener { fetchTestData() }
    }

    private fun insertTestData() {
        Log.d("DB_TEST", "Inserindo dados de teste...")

        // Criando um progresso fictício
        val progress = Progress(money = 500, upgradeId = 1, managerId = 1, achievementsId = 1, toolId = 1)
        progressDAO.saveProgress(progress)

        // Criando um usuário fictício
        val user = User(name = "Jogador Teste", email = "teste@email.com", progressId = 1)
        userDAO.insertUser(user)

        // Criando uma ferramenta fictícia
        val tool = Tool(id = 1, name = "Martelo", amount = 10)
        toolDAO.insertTool(tool)

        // Criando uma conquista fictícia
        val achievement = Achievement(id = 1, name = "Primeira Ferramenta", progress = 100)
        achievementDAO.insertAchievement(achievement)

        // Criando um gerente fictício
        val manager = Manager(id = 1, amount = 3)
        managerDAO.insertManager(manager)

        // Criando um upgrade fictício
        val upgrade = Upgrade(id = 1, name = "Ferramentas Avançadas", progress = 200)
        upgradeDAO.insertUpgrade(upgrade)

        Log.d("DB_TEST", "Dados inseridos com sucesso!")
    }

    private fun fetchTestData() {
        Log.d("DB_TEST", "Buscando dados do banco...")

        val progress = progressDAO.getProgress()
        val user = userDAO.getUser()
        val tools = toolDAO.getTools()
        val achievements = achievementDAO.getAchievements()
        val managers = managerDAO.getManagers()
        val upgrades = upgradeDAO.getUpgrades()

        progress?.let {
            Log.d("DB_TEST", "Progresso - Dinheiro: ${it.money}, Upgrade ID: ${it.upgradeId}")
        } ?: Log.d("DB_TEST", "Nenhum progresso encontrado")

        user?.let {
            Log.d("DB_TEST", "Usuário - Nome: ${it.name}, Email: ${it.email}")
        } ?: Log.d("DB_TEST", "Nenhum usuário encontrado")

        tools.forEach {
            Log.d("DB_TEST", "Ferramenta - ID: ${it.id}, Nome: ${it.name}, Quantidade: ${it.amount}")
        }

        achievements.forEach {
            Log.d("DB_TEST", "Conquista - ID: ${it.id}, Nome: ${it.name}, Progresso: ${it.progress}")
        }

        managers.forEach {
            Log.d("DB_TEST", "Gerente - ID: ${it.id}, Quantidade: ${it.amount}")
        }

        upgrades.forEach {
            Log.d("DB_TEST", "Upgrade - ID: ${it.id}, Nome: ${it.name}, Progresso: ${it.progress}")
        }

        Log.d("DB_TEST", "Busca finalizada!")
    }
}
