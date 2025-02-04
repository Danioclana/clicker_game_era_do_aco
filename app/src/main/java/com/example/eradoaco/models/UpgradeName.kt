package com.example.eradoaco.models

enum class UpgradeName {
    PREGOS_DE_FERRO_DE_QUALIDADE,
    FERRADURA_COM_GUARDA_CASCO,
    ADAGA_RUNICA,
    MACHADOS_BARBAROS_COM_GUME_DE_ACO,
    ARMAS_DE_QUALIDADE_COM_ESCAMAS_DE_DRAGAO,
    KATANA_COM_AFIACAO_MAGICA,
    ARMAS_LENDARIAS_COM_CRISTAIS_DE_ARCANO,
    ARMADURA_DE_PLACA_PERFEITA_DE_FERRO_VULCANICO;

    companion object {
        fun fromString(name: String): UpgradeName? {
            return values().find { it.name == name }
        }
    }
}
