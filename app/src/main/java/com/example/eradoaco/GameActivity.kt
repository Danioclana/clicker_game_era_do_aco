package com.example.eradoaco

import android.animation.ObjectAnimator
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txt_amount_pregos = findViewById<TextView>(R.id.txt_amount_pregos)
        val txt_money_per_second_pregos = findViewById<TextView>(R.id.txt_money_per_second_pregos)
        val txt_money_value = findViewById<TextView>(R.id.txt_money_value)
        val progressbarPregos = findViewById<ProgressBar>(R.id.img_progressbar_pregos)
        val btn_pregos = findViewById<ImageButton>(R.id.btn_pregos)
        var value_pregos = 1
        val btn_buy_pregos = findViewById<FrameLayout>(R.id.btn_buy_pregos)
        val btn_buy_pregos_txt= findViewById<TextView>(R.id.btn_buy_pregos_txt)
        val animator_progressbarPregos = ObjectAnimator.ofInt(progressbarPregos, "progress", 0, 100)
        var timeProductionPregos = 2000L
        val formatador = DecimalFormat("#,##0.00")

        animator_progressbarPregos.duration = timeProductionPregos
        animator_progressbarPregos.interpolator = android.view.animation.AccelerateDecelerateInterpolator()

        btn_pregos.setOnClickListener {
            var moneyValue = txt_money_value.text.toString()
                .replace("$", "")
                .replace(",", ".")
                .trim()

            try {
                val value = if (moneyValue.isNotEmpty()) moneyValue.toDouble() else 0.0

                val updatedValue = value + value_pregos

                animator_progressbarPregos.start()

                animator_progressbarPregos.doOnEnd {
                    txt_money_value.text = "$ " + formatador.format(updatedValue)
                    txt_amount_pregos.text = (txt_amount_pregos.text.toString().toInt() + value_pregos).toString()
                    progressbarPregos.progress = 2
                }

            } catch (e: NumberFormatException) {
                Log.e("Error", "Erro ao converter o valor: $moneyValue", e)
            }
        }

        btn_buy_pregos.setOnClickListener {

            val moneyValue = txt_money_value.text.toString().replace("$", "").trim().toInt()
            val btnPriceValue = btn_buy_pregos_txt.text.toString().replace("$", "").trim().toInt()
            if (moneyValue >= btnPriceValue) {
                txt_amount_pregos.text = (txt_amount_pregos.text.toString().toInt() + 1).toString()
                value_pregos += 1
                val novoValor = btnPriceValue * 2
                btn_buy_pregos_txt.text = "$ " + formatador.format(novoValor)
            }
        }
    }
}