package com.example.eradoaco.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eradoaco.GameActivity
import com.example.eradoaco.GameActivity.GameData
import com.example.eradoaco.R
import com.example.eradoaco.models.GameViewModel
import com.example.eradoaco.models.UpgradeItem

class UpgradeAdapter(
    private var upgradeList: MutableList<UpgradeItem>,
    private val onUpgradePurchased: (UpgradeItem) -> Unit
) : RecyclerView.Adapter<UpgradeAdapter.UpgradeViewHolder>() {

    class UpgradeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img_pregos_upgrade: ImageView = view.findViewById(R.id.img_pregos_upgrade)
        val img_banner_upgrade: TextView = view.findViewById(R.id.img_banner_upgrade)
        val btn_upgrade_txt: TextView = view.findViewById(R.id.btn_upgrade_txt)
        val btn_upgrade: FrameLayout = view.findViewById(R.id.btn_upgrade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpgradeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.upgrade_item, parent, false)
        return UpgradeViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpgradeViewHolder, position: Int) {
        val upgrade = upgradeList[position]

        holder.img_pregos_upgrade.setImageResource(upgrade.imageResId)
        holder.img_banner_upgrade.text = upgrade.description

        val upgradePrice = upgrade.price.replace("$", "").trim().toIntOrNull() ?: 0
        holder.btn_upgrade_txt.text = upgrade.price

        val isAffordable = GameData.money >= upgradePrice
        holder.btn_upgrade.alpha = if (isAffordable) 1.0f else 0.5f
        holder.btn_upgrade.isClickable = isAffordable

        holder.btn_upgrade.setOnClickListener {
            if (isAffordable) {
                GameViewModel.GameManager.updateMoney(GameData.money - upgradePrice)

                when (upgrade.id) {
                    "PREGOS" -> GameData.value_pregos *= 2
                    "PREGOS_2" -> GameData.timeProductionPregos /= 2
                    //"FERRADURAS" -> GameData.value_ferraduras *= 2
                    //"FERRADURAS_2" -> GameData.timeProductionFerraduras /= 2
                    //"ADAGAS" -> GameData.value_adagas *= 2
                    //"ADAGAS_2" -> GameData.timeProductionAdagas /= 2
                }

                GameActivity.ProgressHelper.saveProgress(holder.itemView.context)

                upgradeList.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount() = upgradeList.size
}
