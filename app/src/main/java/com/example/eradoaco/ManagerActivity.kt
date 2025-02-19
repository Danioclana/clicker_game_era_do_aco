package com.example.eradoaco

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manager)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txt_money_value = findViewById(R.id.txt_money_value)
        btn_buy_managers = findViewById(R.id.btn_buy_managers)
        btn_buy_managers_txt = findViewById(R.id.btn_buy_managers_txt)
        txt_amount_managers = findViewById(R.id.txt_amount_managers)
        btn_return = findViewById(R.id.btn_return)


        txt_money_value.text = formatarValor(GameData.money)

        GameViewModel.GameManager.registerMoneyListener { newMoney ->
            txt_money_value.text = formatarValor(newMoney)
        }

        GameViewModel.GameManager.updateMoney(GameData.money)



        btn_buy_managers.setOnClickListener {

            var managerPrice = btn_buy_managers_txt.text.toString()
                .replace("$", "")
                .trim()
                .toInt()

            if (GameData.money >= managerPrice) {
                GameData.money -= managerPrice
                managerPrice *= 1000
                GameData.managers += 1
                txt_amount_managers.text = GameData.managers.toString()
                btn_buy_managers_txt.text = GameActivity.formatarValor(managerPrice)
                txt_money_value.text = GameActivity.formatarValor(GameData.money)
            }
            else {
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

}