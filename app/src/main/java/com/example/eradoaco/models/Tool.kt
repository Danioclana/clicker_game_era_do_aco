package com.example.eradoaco.models

data class Tool(
    val id: Int,
    val tempoProducao: Int,
    val valorProxCompra: Int,
    val quantidade: Int,
    val valor: Int,
)
{
    constructor(tempoProducao: Int, valorProxCompra: Int, quantidade: Int, valor: Int) : this(0, tempoProducao, valorProxCompra, quantidade, valor)
}
