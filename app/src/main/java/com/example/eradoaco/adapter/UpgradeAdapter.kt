package com.example.eradoaco.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eradoaco.R
import com.example.eradoaco.models.UpgradeItem

class UpgradeAdapter(
    private val upgradeList: List<UpgradeItem>,
    private val onItemClick: (UpgradeItem) -> Unit // Callback para cliques
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
        holder.btn_upgrade_txt.text = upgrade.price

        // Adiciona clique ao botão de compra
        holder.btn_upgrade.setOnClickListener {
            onItemClick(upgrade)
        }
    }

    override fun getItemCount() = upgradeList.size
}
