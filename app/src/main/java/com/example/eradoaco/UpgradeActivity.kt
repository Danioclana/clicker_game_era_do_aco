package com.example.eradoaco

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eradoaco.adapter.UpgradeAdapter
import com.example.eradoaco.models.UpgradeItem

class UpgradeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UpgradeAdapter

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
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPref = getSharedPreferences("UPGRADE_PREFS", MODE_PRIVATE)
        val isPregosVisible = sharedPref.getBoolean("PREGOS_VISIBLE", false)

        // Lista de upgrades
        val upgrades = listOf(
            UpgradeItem("PREGOS",R.drawable.tool_prego, "Pregos de Qualidade\nAumenta o ganho em 2x", "$ 5000"),
            UpgradeItem("FERRADURAS",R.drawable.tool_ferradura, "Ferradura de Titânio\nAumenta o ganho em 2x", "$ 10000"),
            UpgradeItem("ADAGAS",R.drawable.tool_adaga, "Adaga Mística\nReduz tempo de produção", "$ 20000")
        )

        val visibleUpgrades = upgrades.filter { upgrade ->
            when (upgrade.id) {
                "PREGOS" -> isPregosVisible
                else -> true
            }
        }

        val adapter = UpgradeAdapter(visibleUpgrades) { selectedUpgrade ->
            // Lógica ao clicar no upgrade
        }

        recyclerView.adapter = adapter

    }
}