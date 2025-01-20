package com.pomplarg.manoeuvredeforceusar

data class VolumeUiState(
    val volume: Double = 0.0,
    val weight: Double = 0.0,
    val density: Int = 0,
    val cf: Double = 0.0,
    val rf: Double = 0.0,
    val emd: Int = 0,
    val nbBrins: Int = 0,
    val crBrins: Double = 0.0,
    val a1: Double = 0.0,
    val a1Activated: Boolean = false,
    val a2Activated: Boolean = false,
    val a3Activated: Boolean = false,
    val safety: Double = 0.0
)

sealed class Volume {
    enum class VolumeType { CONE, CYLINDER }
}