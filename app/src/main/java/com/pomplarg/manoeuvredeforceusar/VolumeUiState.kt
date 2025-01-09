package com.pomplarg.manoeuvredeforceusar

data class VolumeUiState(
    val volume: Double = 0.0,
    val weight: Double = 0.0,
    val density: Int = 0,
    val rf: Double = 0.0
)

sealed class Volume {
    enum class VolumeType { CONE, CYLINDER }
}