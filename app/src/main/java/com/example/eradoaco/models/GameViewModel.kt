package com.example.eradoaco.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eradoaco.GameActivity

class GameViewModel : ViewModel() {

    object GameManager {
        var money: Int = GameActivity.GameData.money
            private set

        private val moneyListeners = mutableListOf<(Int) -> Unit>()

        fun updateMoney(amount: Int) {
            money = amount
            GameActivity.GameData.money = amount

            // Notifica todas as telas registradas que o dinheiro mudou
            moneyListeners.forEach { it(money) }
        }

        fun registerMoneyListener(listener: (Int) -> Unit) {
            moneyListeners.add(listener)
            listener(money) // Atualiza imediatamente ao registrar
        }

        fun unregisterMoneyListener(listener: (Int) -> Unit) {
            moneyListeners.remove(listener)
        }
    }
}
