package com.example.eradoaco

import android.content.Intent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_upgrade)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recycler_view_upgrades)
        txt_money_value = findViewById(R.id.txt_money_value) // Inicializa a variável
        btn_voltar = findViewById(R.id.btn_voltar)
        recyclerView.layoutManager = LinearLayoutManager(this)

        txt_money_value.text = formatarValor(GameData.money)

            GameViewModel.GameManager.registerMoneyListener { newMoney ->
                txt_money_value.text = formatarValor(newMoney)
        }

        GameViewModel.GameManager.updateMoney(GameData.money)


        btn_voltar.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        // Lista de upgrades
        val upgrades = listOf(
            UpgradeItem("PREGOS", R.drawable.tool_prego, "Pregos de Qualidade\nAumenta o ganho em 2x", "$ 1"),
            UpgradeItem("FERRADURAS", R.drawable.tool_ferradura, "Ferradura de Titânio\nAumenta o ganho em 2x", "$ 10000"),
            UpgradeItem("ADAGAS", R.drawable.tool_adaga, "Adaga Mística\nReduz tempo de produção", "$ 20000")
        )


        visibleUpgrades.addAll(upgrades.filter { upgrade ->
            when (upgrade.id) {
                "PREGOS" -> GameData.pregos_upgrades
                else -> false
            }
        })

        adapter = UpgradeAdapter(visibleUpgrades) { selectedUpgrade ->

        }

        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        GameViewModel.GameManager.unregisterMoneyListener { newMoney ->
            txt_money_value.text = formatarValor(newMoney)
        }
    }

}
