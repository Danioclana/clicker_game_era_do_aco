package com.example.eradoaco.models

data class Tool(
    val id: Int,
    val name: ToolName,
    val amount: Int
)
{
    constructor(id: Int, name: String, progress: Int) :
            this(id, ToolName.fromString(name) ?: throw IllegalArgumentException("Ferramenta inválida: $name"), progress)
}

