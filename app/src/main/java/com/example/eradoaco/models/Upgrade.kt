package com.example.eradoaco.models

data class Upgrade(
    val id: Int,
    val name: UpgradeName,
    val progress: Int
) {
    constructor(id: Int, name: String, progress: Int) :
            this(id, UpgradeName.fromString(name) ?: throw IllegalArgumentException("Upgrade inválido: $name"), progress)
}
