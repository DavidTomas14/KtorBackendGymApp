package com.dtalonso.data.util

enum class IncreasinWeightValoration {
    Happy,
    PokerFace,
    Sad
}
fun String.toWeightValoration(): IncreasinWeightValoration? {
    return when (this) {
        "Happy" ->  IncreasinWeightValoration.Happy
        "PokerFace" -> IncreasinWeightValoration.PokerFace
        "Sad" -> IncreasinWeightValoration.Sad
        else -> null
    }
}