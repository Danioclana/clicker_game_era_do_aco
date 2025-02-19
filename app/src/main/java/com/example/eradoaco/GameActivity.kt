package com.example.eradoaco

import android.animation.ObjectAnimator
import android.app.GameManager
import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.eradoaco.UpgradeActivity
import com.example.eradoaco.models.GameViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {

    private lateinit var txt_money_value: TextView
    private var valorCrecimento = 1.20
    private lateinit var btn_managers: ImageButton
    private lateinit var btn_upgrades: ImageButton
    private lateinit var handler: Handler
    private lateinit var handlerPregos: Handler
    private var autoClickPregosAtivo: Boolean = false
    private var autoClickJob: Job? = null


    private lateinit var txt_amount_pregos: TextView
    private lateinit var txt_money_per_second_pregos: TextView
    private lateinit var progressbarPregos: ProgressBar
    private lateinit var btn_pregos: ImageButton
    private lateinit var btn_buy_pregos: FrameLayout
    private lateinit var btn_buy_pregos_txt: TextView
    private lateinit var animator_progressbarPregos: ObjectAnimator


    private lateinit var btn_hide_ferraduras: FrameLayout
    private lateinit var btn_hide_ferraduras_txt: TextView
    private lateinit var txt_amount_ferraduras: TextView
    private lateinit var txt_money_per_second_ferraduras: TextView
    private lateinit var progressbarFerraduras: ProgressBar
    private lateinit var btn_ferraduras: ImageButton
    private var value_ferraduras = 1
    private lateinit var btn_buy_ferraduras: FrameLayout
    private lateinit var btn_buy_ferraduras_txt: TextView
    private lateinit var animator_progressbarFerraduras: ObjectAnimator
    private var timeProductionFerraduras = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        txt_money_value = findViewById(R.id.txt_money_value)
        btn_managers = findViewById(R.id.btn_managers)
        btn_upgrades = findViewById(R.id.btn_upgrades)
        handler = Handler(Looper.getMainLooper())
        handlerPregos = Handler(Looper.getMainLooper())
        autoClickPregosAtivo = false

        txt_amount_pregos = findViewById(R.id.txt_amount_pregos)
        txt_money_per_second_pregos = findViewById(R.id.txt_money_per_second_pregos)
        progressbarPregos = findViewById(R.id.img_progressbar_pregos)
        btn_pregos = findViewById(R.id.btn_pregos)
        btn_buy_pregos = findViewById(R.id.btn_buy_pregos)
        btn_buy_pregos_txt = findViewById(R.id.btn_buy_pregos_txt)

        // Inicializa o animador da progress bar corretamente
        animator_progressbarPregos = ObjectAnimator.ofInt(progressbarPregos, "progress", 0, 100)

        btn_hide_ferraduras = findViewById(R.id.btn_hide_ferraduras)
        btn_hide_ferraduras_txt = findViewById(R.id.btn_hide_ferraduras_txt)
        txt_amount_ferraduras = findViewById(R.id.txt_amount_ferraduras)
        txt_money_per_second_ferraduras = findViewById(R.id.txt_money_per_second_ferraduras)
        progressbarFerraduras = findViewById(R.id.img_progressbar_ferraduras)
        btn_ferraduras = findViewById(R.id.btn_ferraduras)
        btn_buy_ferraduras = findViewById(R.id.btn_buy_ferraduras)
        btn_buy_ferraduras_txt = findViewById(R.id.btn_buy_ferraduras_txt)


        txt_money_value.text = formatarValor(GameData.money)
        animator_progressbarPregos.duration = GameData.timeProductionPregos
        animator_progressbarPregos.interpolator = android.view.animation.AccelerateDecelerateInterpolator()

        startManagers(GameData.managers)


        GameViewModel.GameManager.registerMoneyListener { newMoney ->
            txt_money_value.text = formatarValor(newMoney)
        }


        btn_pregos.setOnClickListener {

            btn_pregos.isEnabled = false

            var txt_money_per_second_pregos_aux = txt_money_per_second_pregos.text.toString()
                .replace("s", "")
                .trim()
                .toInt()

            GameData.money = txt_money_value.text.toString()
                .replace("$", "")
                .trim()
                .toInt()

            try {
                GameData.money += GameData.value_pregos



                animator_progressbarPregos.start()

                animator_progressbarPregos.doOnEnd {
                    txt_money_value.text = formatarValor(GameData.money)
                    progressbarPregos.progress = 2
                    for (i in timeProductionFerraduras downTo 0 step 1000) {
                        txt_money_per_second_pregos.text = "" + (i/1000) + " s"
                    }
                    txt_money_per_second_pregos.text = "" + (timeProductionFerraduras.toInt()/1000) + " s"
                    btn_pregos.isEnabled = true
                }

            } catch (e: NumberFormatException) {
                Log.e("Error", "Erro ao converter o valor: $GameData.money", e)
                btn_pregos.isEnabled = true
            }
        }

        btn_buy_pregos.setOnClickListener {

            GameData.money = txt_money_value.text.toString()
                .replace("$", "")
                .trim()
                .toInt()

            val btnPriceValue = btn_buy_pregos_txt.text.toString()
                .replace("$", "")
                .trim()
                .toInt()


            Log.e("Error", "Erro ao converter o valor: $GameData.money")
            Log.e("Error", "Erro ao converter o valor: $btnPriceValue")

            if (GameData.money >= btnPriceValue) {
                txt_amount_pregos.text = (txt_amount_pregos.text.toString().toInt() + 1).toString()
                GameData.value_pregos = calcularProducao(GameData.value_pregos, txt_amount_pregos.text.toString().toInt(), valorCrecimento)
                val novoValor = calcularCusto(btnPriceValue, txt_amount_pregos.text.toString().toInt(), valorCrecimento)
                btn_buy_pregos_txt.text = formatarValor(novoValor)
                GameViewModel.GameManager.updateMoney(GameData.money - btnPriceValue)
                txt_money_value.text = formatarValor(GameData.money)
            }
        }

        btn_hide_ferraduras.setOnClickListener {
            var moneyValueBtnHideFerraduras = btn_hide_ferraduras_txt.text.toString()
                .replace("$", "")
                .trim()
                .toInt()

            if (txt_money_value.text.toString().replace("$", "").trim().toInt() >= moneyValueBtnHideFerraduras) {
                btn_hide_ferraduras.visibility = FrameLayout.GONE
                btn_hide_ferraduras_txt.visibility = TextView.VISIBLE
                btn_ferraduras.visibility = ImageButton.VISIBLE
                txt_amount_ferraduras.visibility = TextView.VISIBLE
                txt_money_per_second_ferraduras.visibility = TextView.VISIBLE
                progressbarFerraduras.visibility = ProgressBar.VISIBLE
                btn_buy_ferraduras.visibility = FrameLayout.VISIBLE
                btn_buy_ferraduras_txt.visibility = TextView.VISIBLE
            }else{
                Log.e("Error", "Erro ao converter o valor: $moneyValueBtnHideFerraduras")
            }
        }

        btn_managers.setOnClickListener{
            startActivity(Intent(this, ManagerActivity::class.java))
        }

        btn_upgrades.setOnClickListener{
            startActivity(Intent(this, UpgradeActivity::class.java))
        }



    }



    fun iniciarAutoClickPregos() {
        if (GameData.managers != 1) {
            Log.e("Error", "Erro: manager não comprado, valor: ${GameData.managers}")
            return
        }

        if (autoClickPregosAtivo) {
            Log.w("AutoClick", "Já existe um auto-click em andamento!")
            return
        }

        autoClickPregosAtivo = true
        btn_pregos.isEnabled = false

        // Cancela qualquer loop anterior antes de iniciar um novo
        autoClickJob?.cancel()

        autoClickJob = CoroutineScope(Dispatchers.Main).launch {
            while (autoClickPregosAtivo) {
                progressbarPregos.progress = 0  // Reset para garantir que a barra reinicie
                animator_progressbarPregos.cancel()  // Cancela qualquer animação anterior
                animator_progressbarPregos.duration = GameData.timeProductionPregos
                animator_progressbarPregos.start()

                animator_progressbarPregos.doOnEnd {
                    btn_pregos.isEnabled = true
                }

                delay(GameData.timeProductionPregos)  // Espera o tempo de produção

                GameData.money += GameData.value_pregos
                GameViewModel.GameManager.updateMoney(GameData.money)

                txt_money_value.text = formatarValor(GameData.money)
                txt_money_per_second_pregos.text = "${GameData.timeProductionPregos / 1000} s"
            }
        }
    }

    fun startManagers(managers: Int) {

        if (managers > 0) {
            handler.postDelayed({
                iniciarAutoClickPregos()
            }, 1000)
        }
    }

    fun calcularCusto(baseCusto: Int, quantidade: Int, fatorCrescimento: Double): Int {
        return (baseCusto * Math.pow((fatorCrescimento), quantidade.toDouble())).toInt()
    }

    fun calcularProducao(baseProducao: Int, quantidade: Int, fatorCrescimento: Double): Int {
        return (baseProducao * Math.pow(fatorCrescimento, quantidade.toDouble())).toInt()
    }

    fun calcularTempoFabricacao(tempoBase: Int, quantidade: Int): Int {
        val reducoes = quantidade / 50
        return tempoBase / Math.pow(2.0, reducoes.toDouble()).toInt().coerceAtLeast(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        GameViewModel.GameManager.unregisterMoneyListener { newMoney ->
            txt_money_value.text = formatarValor(newMoney)
        }
    }


    companion object {
        fun formatarValor(valor: Int): String {
            val unidades = arrayOf(
                "Milhão",
                "Bilhão",
                "Trilhão",
                "Quadrilhão",
                "Quintilhão",
                "Sextilhão",
                "Septilhão",
                "Octilhão",
                "Nonilhão",
                "Decilhão",
                "Undecilhão",
                "Dodecilhão",
                "Tredecilhão",
                "Quatordecilhão",
                "Quindecilhão",
                "Sedecilhão",
                "Septendecilhão",
                "Octodecilhão",
                "Nonidecilhão",
                "Vigintilhão",
                "Unvigintilhão",
                "Dovigintilhão",
                "Trevigintilhão",
                "Quatuorvigintilhão",
                "Quinvigintilhão",
                "Sesvigintilhão",
                "Septenvigintilhão",
                "Octovigintilhão",
                "Nonivigintilhão",
                "Trigintilhão",
                "Untrigintilhão",
                "Dotrigintilhão"
            )

            if (valor < 1_000_000) return "$ $valor"

            var valorFormatado = valor.toDouble()
            var index = 0

            while (valorFormatado >= 1000 && index < unidades.size) {
                valorFormatado /= 1000
                index++
            }

            return String.format("$ ", valorFormatado, unidades[index])
        }
    }

    object GameData {
        var managers: Int = 0
        var money: Int = 0
        var pregos_upgrades: Boolean = true
        var value_pregos: Int = 1
        var timeProductionPregos: Long = 2000L

    }
}