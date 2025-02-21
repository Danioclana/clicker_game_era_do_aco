package com.example.eradoaco

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eradoaco.database.ManagerDAO
import com.example.eradoaco.database.MoneyDAO
import com.example.eradoaco.models.GameViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.eradoaco.database.ToolDAO
import com.example.eradoaco.models.Manager
import com.example.eradoaco.models.Money
import com.example.eradoaco.models.Tool

@SuppressLint("SetTextI18n")
class GameActivity : AppCompatActivity() {

    private lateinit var txt_money_value: TextView
    private var valorCrecimento = 1.20
    private lateinit var btn_managers: ImageButton
    private lateinit var btn_upgrades: ImageButton
    private lateinit var btn_return: ImageButton
    private lateinit var btn_game_config: ImageButton
    private lateinit var handler: Handler
    private lateinit var handlerPregos: Handler
    private var autoClickPregosAtivo: Boolean = false
    private var autoClickFerradurasAtivo: Boolean = false
    private var autoClickJob: Job? = null
    private lateinit var menu_config: ConstraintLayout

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
    private lateinit var btn_buy_ferraduras: FrameLayout
    private lateinit var btn_buy_ferraduras_txt: TextView
    private lateinit var animator_progressbarFerraduras: ObjectAnimator

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
        menu_config = findViewById(R.id.menu_config)
        btn_return = findViewById(R.id.btn_return)
        handler = Handler(Looper.getMainLooper())
        handlerPregos = Handler(Looper.getMainLooper())
        autoClickPregosAtivo = false
        autoClickFerradurasAtivo = false
        btn_game_config = findViewById(R.id.btn_game_config)

        txt_amount_pregos = findViewById(R.id.txt_amount_pregos)
        txt_money_per_second_pregos = findViewById(R.id.txt_money_per_second_pregos)
        progressbarPregos = findViewById(R.id.img_progressbar_pregos)
        btn_pregos = findViewById(R.id.btn_pregos)
        btn_buy_pregos = findViewById(R.id.btn_buy_pregos)
        btn_buy_pregos_txt = findViewById(R.id.btn_buy_pregos_txt)
        animator_progressbarPregos = ObjectAnimator.ofInt(progressbarPregos, "progress", 0, 100)

        btn_hide_ferraduras = findViewById(R.id.btn_hide_ferraduras)
        btn_hide_ferraduras_txt = findViewById(R.id.btn_hide_ferraduras_txt)
        txt_amount_ferraduras = findViewById(R.id.txt_amount_ferraduras)
        txt_money_per_second_ferraduras = findViewById(R.id.txt_money_per_second_ferraduras)
        progressbarFerraduras = findViewById(R.id.img_progressbar_ferraduras)
        btn_ferraduras = findViewById(R.id.btn_ferraduras)
        btn_buy_ferraduras = findViewById(R.id.btn_buy_ferraduras)
        btn_buy_ferraduras_txt = findViewById(R.id.btn_buy_ferraduras_txt)
        animator_progressbarFerraduras = ObjectAnimator.ofInt(progressbarFerraduras, "progress", 0, 100)

        ProgressHelper.loadProgress(this)

        val prefs = getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        val ferradurasVisible = prefs.getBoolean("ferraduras_visible", false)

        if (ferradurasVisible) {
            btn_hide_ferraduras.visibility = View.GONE
            btn_hide_ferraduras_txt.visibility = View.VISIBLE
            btn_ferraduras.visibility = View.VISIBLE
            txt_amount_ferraduras.visibility = View.VISIBLE
            txt_money_per_second_ferraduras.visibility = View.VISIBLE
            progressbarFerraduras.visibility = View.VISIBLE
            btn_buy_ferraduras.visibility = View.VISIBLE
            btn_buy_ferraduras_txt.visibility = View.VISIBLE
        }

        updatePregos()
        updateFerraduras()
        updateMoney()
        updateManagers()

        txt_money_value.text = formatarValor(GameData.money)
        animator_progressbarPregos.duration = GameData.timeProductionPregos
        animator_progressbarPregos.interpolator = android.view.animation.AccelerateDecelerateInterpolator()

        GameViewModel.GameManager.updateMoney(GameData.money)

        GameViewModel.GameManager.registerMoneyListener { newMoney ->
            txt_money_value.text = formatarValor(newMoney)
        }

        btn_managers.setOnClickListener{
            startActivity(Intent(this, ManagerActivity::class.java))
        }

        btn_upgrades.setOnClickListener{
            startActivity(Intent(this, UpgradeActivity::class.java))
        }

        btn_game_config.setOnClickListener {
            menu_config.visibility = View.VISIBLE
        }

        btn_managers.setOnClickListener {
            startActivity(Intent(this, ManagerActivity::class.java))
            menu_config.visibility = View.GONE
        }

        btn_upgrades.setOnClickListener {
            startActivity(Intent(this, UpgradeActivity::class.java))
            menu_config.visibility = View.GONE
        }

        btn_return.setOnClickListener {
            menu_config.visibility = View.GONE
        }

        btn_pregos.setOnClickListener {

            btn_pregos.isEnabled = false

            txt_money_per_second_pregos.text.toString()
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
                    for (i in GameData.timeProductionPregos downTo 0 step 1000) {
                        txt_money_per_second_pregos.text = "" + (i/1000) + " s"
                    }
                    txt_money_per_second_pregos.text = "" + (GameData.timeProductionPregos.toInt()/1000) + " s"
                    btn_pregos.isEnabled = true
                }

                ProgressHelper.saveProgress(this)
                Log.d("ANIMATION", "Pregos: ${animator_progressbarPregos.duration}, Ferraduras: ${animator_progressbarFerraduras.duration}")

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

            GameData.valorProxCompraPrego = btn_buy_pregos_txt.text.toString()
                .replace("$", "")
                .trim()
                .toInt()

            if (GameData.money >= GameData.valorProxCompraPrego) {
                txt_amount_pregos.text = (txt_amount_pregos.text.toString().toInt() + 1).toString()
                GameData.amount_pregos = txt_amount_pregos.text.toString().toInt()
                GameData.value_pregos = calcularProducao(GameData.value_pregos, txt_amount_pregos.text.toString().toInt(), valorCrecimento)
                GameData.valorProxCompraPrego = calcularCusto(GameData.valorProxCompraPrego, txt_amount_pregos.text.toString().toInt(), valorCrecimento)
                btn_buy_pregos_txt.text = formatarValor(GameData.valorProxCompraPrego)
                GameViewModel.GameManager.updateMoney(GameData.money - GameData.valorProxCompraPrego)
                txt_money_value.text = formatarValor(GameData.money)
            }

            ProgressHelper.saveProgress(this)
        }

        btn_hide_ferraduras.setOnClickListener {
            GameData.ferraduras_upgrades = true
            val moneyValueBtnHideFerraduras = btn_hide_ferraduras_txt.text.toString()
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

                val prefs = getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
                prefs.edit().putBoolean("ferraduras_visible", true).apply()
            }else{
                Log.e("Error", "Erro ao converter o valor: $moneyValueBtnHideFerraduras")
            }
        }

        btn_ferraduras.setOnClickListener {

            btn_ferraduras.isEnabled = false

            txt_money_per_second_ferraduras.text.toString()
                .replace("s", "")
                .trim()
                .toInt()

            GameData.money = txt_money_value.text.toString()
                .replace("$", "")
                .trim()
                .toInt()

            try {
                GameData.money += GameData.value_ferraduras
                animator_progressbarFerraduras.start()

                animator_progressbarFerraduras.doOnEnd {
                    txt_money_value.text = formatarValor(GameData.money)
                    progressbarFerraduras.progress = 2
                    for (i in GameData.timeProductionFerraduras downTo 0 step 1000) {
                        txt_money_per_second_ferraduras.text = "" + (i/1000) + " s"
                    }
                    txt_money_per_second_ferraduras.text = "" + (GameData.timeProductionFerraduras.toInt()/1000) + " s"
                    btn_ferraduras.isEnabled = true
                }

                ProgressHelper.saveProgress(this)
                Log.d("ANIMATION", "Pregos: ${animator_progressbarPregos.duration}, Ferraduras: ${animator_progressbarFerraduras.duration}")

            } catch (e: NumberFormatException) {
                Log.e("Error", "Erro ao converter o valor: $GameData.money", e)
                btn_ferraduras.isEnabled = true
            }
        }

        btn_buy_ferraduras.setOnClickListener {
            GameData.money = txt_money_value.text.toString()
                .replace("$", "")
                .trim()
                .toInt()

            GameData.valorProxCompraFerraduras = btn_buy_ferraduras_txt.text.toString()
                .replace("$", "")
                .trim()
                .toInt()

            if (GameData.money >= GameData.valorProxCompraFerraduras) {
                txt_amount_ferraduras.text = (txt_amount_ferraduras.text.toString().toInt() + 1).toString()
                GameData.amount_ferraduras = txt_amount_ferraduras.text.toString().toInt()
                GameData.value_ferraduras = calcularProducao(GameData.value_ferraduras, txt_amount_ferraduras.text.toString().toInt(), valorCrecimento)
                GameData.valorProxCompraFerraduras = calcularCusto(GameData.valorProxCompraFerraduras, txt_amount_ferraduras.text.toString().toInt(), valorCrecimento)
                btn_buy_ferraduras_txt.text = formatarValor(GameData.valorProxCompraFerraduras)
                GameViewModel.GameManager.updateMoney(GameData.money - GameData.valorProxCompraFerraduras)
                txt_money_value.text = formatarValor(GameData.money)
            }

            ProgressHelper.saveProgress(this)
        }
    }

    fun iniciarAutoClickPregos() {
        if (GameData.managers < 1) {
            Log.e("Error", "Erro: manager não comprado, valor: ${GameData.managers}")
            return
        }

        if (autoClickPregosAtivo) {
            Log.w("AutoClick", "Já existe um auto-click em andamento!")
            return
        }

        autoClickPregosAtivo = true
        btn_pregos.isEnabled = false

        autoClickJob?.cancel()

        autoClickJob = CoroutineScope(Dispatchers.Main).launch {
            while (autoClickPregosAtivo) {
                progressbarPregos.progress = 0
                animator_progressbarPregos.cancel()
                animator_progressbarPregos.duration = GameData.timeProductionPregos
                animator_progressbarPregos.start()

                animator_progressbarPregos.doOnEnd {
                    btn_pregos.isEnabled = true
                }

                delay(GameData.timeProductionPregos)
                GameData.money += GameData.value_pregos
                GameViewModel.GameManager.updateMoney(GameData.money)
                ProgressHelper.saveProgress(this@GameActivity)

                txt_money_value.text = formatarValor(GameData.money)
                txt_money_per_second_pregos.text = "${GameData.timeProductionPregos / 1000} s"
            }
        }
    }

    fun iniciarAutoClickFerraduras() {
        if (GameData.managers < 1) {
            Log.e("Error", "Erro: manager não comprado, valor: ${GameData.managers}")
            return
        }

        if (autoClickFerradurasAtivo) {
            Log.w("AutoClick", "Já existe um auto-click em andamento!")
            return
        }

        autoClickFerradurasAtivo = true
        btn_ferraduras.isEnabled = false

        autoClickJob?.cancel()

        autoClickJob = CoroutineScope(Dispatchers.Main).launch {
            while (autoClickFerradurasAtivo) {
                progressbarFerraduras.progress = 0
                animator_progressbarFerraduras.cancel()
                animator_progressbarFerraduras.duration = GameData.timeProductionFerraduras
                animator_progressbarFerraduras.start()

                animator_progressbarFerraduras.doOnEnd {
                    btn_pregos.isEnabled = true
                }

                delay(GameData.timeProductionFerraduras)
                GameData.money += GameData.value_ferraduras
                GameViewModel.GameManager.updateMoney(GameData.money)
                ProgressHelper.saveProgress(this@GameActivity)

                txt_money_value.text = formatarValor(GameData.money)
                txt_money_per_second_ferraduras.text = "${GameData.timeProductionFerraduras / 1000} s"
            }
        }
    }


    fun calcularCusto(baseCusto: Int, quantidade: Int, fatorCrescimento: Double): Int {
        return (baseCusto * Math.pow((fatorCrescimento), quantidade.toDouble())).toInt()
    }

    fun calcularProducao(baseProducao: Int, quantidade: Int, fatorCrescimento: Double): Int {
        return (baseProducao * Math.pow(fatorCrescimento, quantidade.toDouble())).toInt()
    }

    fun updatePregos() {
        txt_amount_pregos.text = ("${GameData.amount_pregos}")
        txt_money_per_second_pregos.text = "${GameData.timeProductionPregos / 1000} s"
        btn_buy_pregos_txt.text = formatarValor(GameData.valorProxCompraPrego)
    }

    fun updateFerraduras() {

        txt_amount_ferraduras.text = ("${GameData.amount_ferraduras}")
        txt_money_per_second_ferraduras.text = "${GameData.timeProductionFerraduras / 1000} s"
        btn_buy_ferraduras_txt.text = formatarValor(GameData.valorProxCompraFerraduras)
    }

    fun updateMoney() {
        txt_money_value.text = formatarValor(GameData.money)
    }

    fun updateManagers() {


        if (GameData.managers > 0) {
            handler.postDelayed({
                iniciarAutoClickPregos()
            }, 1000)
        }
        if (GameData.managers > 1 && GameData.ferraduras_upgrades) {
            handler.postDelayed({
                iniciarAutoClickFerraduras()
            }, 1000)
        }
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

            var valorFormatado = valor
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
        var adagas_upgrades: Boolean = false
        var next_manager_price: Int = 0

        var valorProxCompraPrego: Int = 1
        var value_pregos: Int = 1
        var pregos_upgrades: Boolean = true
        var timeProductionPregos: Long = 2000L
        var amount_pregos: Int = 1

        var valorProxCompraFerraduras: Int = 1
        var value_ferraduras: Int = 1
        var ferraduras_upgrades: Boolean = false
        var timeProductionFerraduras: Long = 2000L
        var amount_ferraduras: Int = 1
    }

    object ProgressHelper {
        fun saveProgress(context: Context) {
            val prego: Tool = Tool(1, GameData.timeProductionPregos.toInt(),GameData.valorProxCompraPrego, GameData.amount_pregos, GameData.value_pregos)
            val ferraduras: Tool = Tool(2, GameData.timeProductionFerraduras.toInt(),GameData.valorProxCompraFerraduras, GameData.amount_ferraduras, GameData.value_ferraduras)
            ToolDAO(context).saveOrUpdateTool(prego)
            ToolDAO(context).saveOrUpdateTool(ferraduras)

            val money: Money = Money(GameData.money)
            MoneyDAO(context).saveOrUpdateMoney(money.money)

            val manager: Manager = Manager(1, GameData.managers)
            ManagerDAO(context).saveOrUpdateManager(manager)
        }

        fun loadProgress(context: Context) {
            val tools = ToolDAO(context).getTools()
            val money = MoneyDAO(context).getMoney()
            val managers = ManagerDAO(context).getManagers()

            if (tools.isNotEmpty()) {
                GameActivity.GameData.timeProductionPregos = tools[0].tempoProducao.toLong()
                GameActivity.GameData.valorProxCompraPrego = tools[0].valorProxCompra
                GameActivity.GameData.amount_pregos = tools[0].quantidade
                GameActivity.GameData.value_pregos = tools[0].valor

                GameActivity.GameData.timeProductionFerraduras = tools[1].tempoProducao.toLong()
                GameActivity.GameData.valorProxCompraFerraduras = tools[1].valorProxCompra
                GameActivity.GameData.amount_ferraduras = tools[1].quantidade
                GameActivity.GameData.value_ferraduras = tools[1].valor

            }
            if (money != 0) {
                GameActivity.GameData.money = money
            }
            if (managers.isNotEmpty()) {
                GameActivity.GameData.managers = managers[0].amount
            }
        }
    }
}