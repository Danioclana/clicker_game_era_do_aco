package com.example.eradoaco

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eradoaco.GameActivity.Companion.formatarValor
import com.example.eradoaco.GameActivity.GameData
import com.example.eradoaco.adapter.UpgradeAdapter
import com.example.eradoaco.models.GameViewModel
import com.example.eradoaco.models.UpgradeItem

class UpgradeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UpgradeAdapter
    private lateinit var txt_money_value: TextView
    private val visibleUpgrades = mutableListOf<UpgradeItem>()
    private lateinit var btn_voltar: ImageButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btn_game_config: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_upgrade)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("GameData", Context.MODE_PRIVATE)

        recyclerView = findViewById(R.id.recycler_view_upgrades)
        txt_money_value = findViewById(R.id.txt_money_value)
        btn_voltar = findViewById(R.id.btn_voltar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        btn_game_config = findViewById(R.id.btn_game_config)

        txt_money_value.text = formatarValor(GameData.money)

        btn_game_config.setOnClickListener {

        }

        GameViewModel.GameManager.registerMoneyListener { newMoney ->
            txt_money_value.text = formatarValor(newMoney)
        }

        GameViewModel.GameManager.updateMoney(GameData.money)

        btn_voltar.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        val upgrades = listOf(
            UpgradeItem("PREGOS", R.drawable.tool_prego, "Pregos de Qualidade\nAumenta o ganho em 2x", "$ 1"),
            UpgradeItem("PREGOS_2", R.drawable.tool_prego, "Pregos leves\nReduz tempo de produção pela metade", "$ 1"),
            UpgradeItem("FERRADURAS", R.drawable.tool_ferradura, "Ferradura de Titânio\nAumenta o ganho em 2x", "$ 10000"),
            UpgradeItem("FERRADURAS_2", R.drawable.tool_ferradura, "Ferradura de aluminio\nReduz tempo de produção pela metade", "$ 10000"),
            UpgradeItem("ADAGAS", R.drawable.tool_adaga, "Adaga Mística\nAumenta o ganho em 2x", "$ 20000"),
            UpgradeItem("ADAGAS_2", R.drawable.tool_adaga, "Adaga arremessáveis\nReduz tempo de produção pela metade", "$ 20000")
        )

        val purchasedUpgrades = loadPurchasedUpgrades()

        visibleUpgrades.addAll(upgrades.filter { upgrade -> !purchasedUpgrades.contains(upgrade.id) })

        adapter = UpgradeAdapter(visibleUpgrades, ::onUpgradePurchased)

        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        GameViewModel.GameManager.unregisterMoneyListener { newMoney ->
            txt_money_value.text = formatarValor(newMoney)
        }
    }

    private fun onUpgradePurchased(upgrade: UpgradeItem) {
        val purchasedUpgrades = loadPurchasedUpgrades().toMutableSet()
        purchasedUpgrades.add(upgrade.id)
        savePurchasedUpgrades(purchasedUpgrades)
    }

    private fun savePurchasedUpgrades(upgrades: Set<String>) {
        val editor = sharedPreferences.edit()
        editor.putStringSet("purchasedUpgrades", upgrades)
        editor.apply()
    }

    private fun loadPurchasedUpgrades(): Set<String> {
        return sharedPreferences.getStringSet("purchasedUpgrades", emptySet()) ?: emptySet()
    }
}
