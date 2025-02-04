package com.example.eradoaco.models

enum class ToolName {
    PREGO,
    FERRADURA,
    ADAGA,
    MACHADOS_BARBAROS,
    ARMAS_QUALIDADE,
    ARMAS_SAMURAI,
    ARMAS_LENDARIAS,
    ARMADURA_PLACA_PERFEITA;

    companion object {
        fun fromString(name: String): ToolName? {
            return values().find { it.name == name
            }
        }
    }
}