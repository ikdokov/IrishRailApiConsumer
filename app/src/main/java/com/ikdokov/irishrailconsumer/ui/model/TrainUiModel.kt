package com.ikdokov.irishrailconsumer.ui.model

data class TrainUiModel(
    val expArrival: String,
    val trainCode: String,
    val destination: String,
    val arivalTimeLeft: Long
)