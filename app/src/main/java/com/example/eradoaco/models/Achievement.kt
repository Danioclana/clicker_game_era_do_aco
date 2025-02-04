package com.example.eradoaco.models

data class Achievement(
    val id: Int,
    val name: AchievementName,
    val progress: Int
){
    constructor(id: Int, name: String, progress: Int) :
            this(id, AchievementName.fromString(name) ?: throw IllegalArgumentException("Conquista inválida: $name"), progress)
}