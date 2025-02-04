package com.example.eradoaco.models

enum class AchievementName() {
    PRIMEIRO_PREGO,
    MESTRE_DA_FERRADURA,
    GUERREIRO_DAS_SOMBRAS,
    LENHADOR_BARBARO,
    FORJADOR_DE_LENDAS,
    MESTRE_SAMURAI,
    GUARDIAO_DAS_ARMADURAS,
    SENHOR_DOS_MACHADOS,
    FERREIRO_EXPERIENTE,
    LENDARIO_ARTESAO,
    DOMINADOR_DO_AÇO,
    FORJA_MAGICA,
    ESPIRITO_DO_DRAGAO,
    MESTRE_DAS_ARMAS,
    COLECIONADOR_DE_ARTEFATOS,
    REI_DA_BATALHA;

    companion object {
        fun fromString(name: String): AchievementName? {
            return values().find { it.name == name
            }
        }
    }
}
