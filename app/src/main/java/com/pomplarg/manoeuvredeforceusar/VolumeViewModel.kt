package com.pomplarg.manoeuvredeforceusar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.PI
import kotlin.math.pow

class VolumeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(VolumeUiState())
    val uiState: StateFlow<VolumeUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = VolumeUiState(
        )
    }

    fun updateVolume(volumeType: Volume.VolumeType, rayon: Double, hauteur: Double) {
        val volume = when(volumeType) {
            Volume.VolumeType.CONE -> {
                PI * rayon
                    .pow(2) * hauteur * 1 / 3
            }

            Volume.VolumeType.CYLINDER -> {
                PI * rayon
                    .pow(2) * hauteur
            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                volume =  volume
            )
        }
        updateWeight(volume, _uiState.value.density)
    }

    fun updateWeight(volume: Double, density: Int?) {
        var weight = 0.0
        density?.let {
            weight = volume * it
        }
        _uiState.update { currentState ->
            currentState.copy(
                weight =  weight
            )
        }
    }

    fun updateDensity(density: Int?) {
        density?.let {
            _uiState.update { currentState ->
                currentState.copy(
                    density = density
                )
            }
        }
    }

    fun updateRF(poids: Double, cf: Double?) {
        cf?.let {
            val rf = poids * cf
            _uiState.update { currentState ->
                currentState.copy(
                    rf = rf
                )
            }
        }
    }

    fun updateWeightWithEntry(entry: String) {
        _uiState.update { currentState ->
            currentState.copy(
                weight =  entry.toDouble()
            )
        }
    }
}