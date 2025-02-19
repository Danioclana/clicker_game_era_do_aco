package com.example.eradoaco

import android.animation.ObjectAnimator
import android.app.GameManager
import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eradoaco.database.ProgressDAO
import com.example.eradoaco.models.Progress

class GameActivity : AppCompatActivity() {

    private lateinit var txt_money_value: TextView
    private var valorCrecimento = 1.20
    private lateinit var btn_managers: ImageButton
    private lateinit var btn_upgrades: ImageButton
    private lateinit var handler: Handler
    private lateinit var handlerPregos: Handler


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

        loadProgress()

        txt_money_value.text = formatarValor(GameData.money)
        animator_progressbarPregos.duration = GameData.timeProductionPregos
        animator_progressbarPregos.interpolator = AccelerateDecelerateInterpolator()

        startManagers(GameData.managers)

        val sharedPref = getSharedPreferences("UPGRADE_PREFS", MODE_PRIVATE)
        sharedPref.edit().putBoolean("PREGOS_VISIBLE", true).apply()
        sharedPref.edit().putBoolean("FERRADURAS_VISIBLE", false).apply()
        sharedPref.edit().putBoolean("ADAGAS_VISIBLE", false).apply()

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

                saveProgress()

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
                GameData.money -= btnPriceValue
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
        if (GameData.managers == 1) {
            btn_pregos.isEnabled = false

            val txtMoneyPerSecondPregosAux = txt_money_per_second_pregos.text.toString()
                .replace("s", "")
                .trim()
                .toIntOrNull() ?: 0  // Evita erro de conversão

            GameData.money = txt_money_value.text.toString()
                .replace("$", "")
                .trim()
                .toIntOrNull() ?: 0  // Evita erro de conversão

            try {
                // Adiciona dinheiro de forma controlada
                GameData.money += GameData.value_pregos


                // Atualiza UI (na thread principal)
                txt_money_value.post {
                    txt_money_value.text = formatarValor(GameData.money)
                }

                // Reinicia a progress bar corretamente antes da nova animação
                progressbarPregos.progress = 0
                animator_progressbarPregos.cancel() // Cancela a animação anterior se houver

                // Configura e inicia a animação da progress bar
                animator_progressbarPregos.duration = GameData.timeProductionPregos
                animator_progressbarPregos.start()

                animator_progressbarPregos.doOnEnd {
                    txt_money_value.text = formatarValor(GameData.money)
                    progressbarPregos.progress = 0
                    txt_money_per_second_pregos.text = "${GameData.timeProductionPregos / 1000} s"

                    btn_pregos.isEnabled = true

                    // Agendar a próxima execução de forma segura
                    handlerPregos.postDelayed({ iniciarAutoClickPregos() }, GameData.timeProductionPregos)
                    saveProgress()
                }
            } catch (e: NumberFormatException) {
                Log.e("Error", "Erro ao converter o valor: $GameData.money", e)
                btn_pregos.isEnabled = true
            }
        } else {
            Log.e("Error", "Erro: manager não comprado, valor: ${GameData.managers}")
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
        var pregos_upgrades: Int = 0
        var value_pregos: Int = 1
        var timeProductionPregos: Long = 2000L
        var achievementsId: Int = 0
        var toolId: Int = 1

    }

    fun saveProgress() {
        val progressDAO = ProgressDAO(this)
        val progress = Progress(
            GameData.money,
            GameData.pregos_upgrades,
            GameData.managers,
            GameData.achievementsId,
            GameData.toolId
        )
        progressDAO.saveOrUpdateProgress(progress)
        Log.d("Progress", "Progresso salvo: $progress")
    }

    fun loadProgress() {
        val progressDAO = ProgressDAO(this)
        val progress = progressDAO.getProgress()
        if (progress != null) {
            GameData.money = progress.money
            GameData.pregos_upgrades = progress.upgradeId
            GameData.managers = progress.managerId
        }
        Log.d("Progress", "Progresso carregado: $progress")
    }
}