package com.example.eradoaco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eradoaco.GameActivity.GameData
import com.example.eradoaco.models.GameViewModel
import com.example.eradoaco.GameActivity.Companion.formatarValor

class ManagerActivity : AppCompatActivity() {

    private lateinit var txt_money_value: TextView
    private lateinit var btn_buy_managers: FrameLayout
    private lateinit var btn_buy_managers_txt: TextView
    private lateinit var txt_amount_managers: TextView
    private lateinit var btn_return: ImageButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btn_game_config: ImageButton
    private lateinit var menu_config: FrameLayout
    private lateinit var btn_upgrades: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manager)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("GameData", Context.MODE_PRIVATE)

        txt_money_value = findViewById(R.id.txt_money_value)
        btn_buy_managers = findViewById(R.id.btn_buy_managers)
        btn_buy_managers_txt = findViewById(R.id.btn_buy_managers_txt)
        txt_amount_managers = findViewById(R.id.txt_amount_managers)
        btn_return = findViewById(R.id.btn_return)
        btn_game_config = findViewById(R.id.btn_game_config)
        menu_config = findViewById(R.id.menu_config)
        btn_upgrades = findViewById(R.id.btn_upgrades)

        carregarDados()

        txt_money_value.text = formatarValor(GameData.money)

        GameViewModel.GameManager.registerMoneyListener { newMoney ->
            txt_money_value.text = formatarValor(newMoney)
        }

        btn_game_config.setOnClickListener {
            menu_config.visibility = View.VISIBLE
        }

        btn_upgrades.setOnClickListener {
            startActivity(Intent(this, UpgradeActivity::class.java))
            menu_config.visibility = View.GONE
        }

        btn_return.setOnClickListener {
            menu_config.visibility = View.GONE
        }

        GameViewModel.GameManager.updateMoney(GameData.money)

        btn_buy_managers.setOnClickListener {
            GameData.next_manager_price = btn_buy_managers_txt.text.toString()
                .replace("$", "")
                .trim()
                .toInt()

            if (GameData.money >= GameData.next_manager_price) {
                GameData.money -= GameData.next_manager_price
                GameData.next_manager_price *= 1000
                GameData.managers += 1

                txt_amount_managers.text = GameData.managers.toString()
                btn_buy_managers_txt.text = GameActivity.formatarValor(GameData.next_manager_price)
                txt_money_value.text = GameActivity.formatarValor(GameData.money)

                // Salva os dados sempre que houver uma compra
                salvarDados()
            } else {
                Log.e("ManagerActivity", "Not enough money")
            }
        }

        btn_return.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GameViewModel.GameManager.unregisterMoneyListener { newMoney ->
            txt_money_value.text = formatarValor(newMoney)
        }
    }

    private fun salvarDados() {
        val editor = sharedPreferences.edit()
        editor.putInt("money", GameData.next_manager_price)
        editor.putInt("managers", GameData.managers)
        editor.apply()
    }

    private fun carregarDados() {
        GameData.next_manager_price = sharedPreferences.getInt("money", GameData.next_manager_price)
        GameData.managers = sharedPreferences.getInt("managers", GameData.managers)

        btn_buy_managers_txt.text = formatarValor(GameData.next_manager_price)
        txt_amount_managers.text = GameData.managers.toString()
    }
}
