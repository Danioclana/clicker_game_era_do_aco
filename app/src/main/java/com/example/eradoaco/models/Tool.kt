package com.example.eradoaco.models

data class Tool(
    val id: Int,
    val tempoProducao: Int,
    val valorProxCompra: Int,
    val quatidade: Int,
)
{
    constructor(id: Int, tempoProducao: Int, valorProxCompra: Int) : this(id, tempoProducao, valorProxCompra, 0)
}
