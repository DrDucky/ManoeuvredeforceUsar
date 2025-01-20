package com.pomplarg.manoeuvredeforceusar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.pow

class VolumeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(VolumeUiState())
    val uiState: StateFlow<VolumeUiState> = _uiState.asStateFlow()
    private val crbrins = mapOf(1 to 1.0, 2 to 1.9, 3 to 2.75, 4 to 3.44, 5 to 4.09)

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


    fun updateDensity(density: Int?) {
        density?.let {
            _uiState.update { currentState ->
                currentState.copy(
                    density = density
                )
            }
        }
        updateWeight(_uiState.value.volume, density)
    }

    fun updateRF(cf: Double?) {
        cf?.let {
            val rf = _uiState.value.weight * cf
            _uiState.update { currentState ->
                currentState.copy(
                    cf = cf,
                    rf = rf
                )
            }
            updateNbBrins(rf, _uiState.value.emd)
        }
        updateSecurity()
    }

    private fun updateWeight(volume: Double, density: Int?) {
        var weight = 0.0
        density?.let {
            weight = volume * it
        }
        _uiState.update { currentState ->
            currentState.copy(
                weight =  weight
            )
        }
        updateRF(_uiState.value.cf)
    }

    fun updateWeightWithManualEntry(entry: String) {
        if(entry.isNotBlank()) {
            _uiState.update { currentState ->
                currentState.copy(
                    weight = entry.toDouble()
                )
            }
        }
        updateRF(_uiState.value.cf)
    }

    fun updateEmd(emd: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                emd =  emd
            )
        }
        updateNbBrins(_uiState.value.rf, emd)
        updateSecurity()
    }

    fun updateNbBrins(rf: Double, emd: Int) {
        val nbBrins = (ceil(rf / emd).toInt() + 1).coerceAtLeast(0)
        _uiState.update { currentState ->
            currentState.copy(
                nbBrins =  nbBrins,
            )
        }
        crbrins[nbBrins]?.let {
            _uiState.update { currentState ->
                currentState.copy(
                    crBrins = it
                )
            }
        }
        when {
            nbBrins <= 0 -> _uiState.update { currentState ->
                currentState.copy(
                    a1Activated =  false,
                    a2Activated = false,
                    a3Activated = false
                )
            }
             nbBrins == 1 -> _uiState.update { currentState ->
                currentState.copy(
                    a1Activated =  true,
                    a2Activated = false,
                    a3Activated = false
                )
            }
            nbBrins == 2 -> _uiState.update { currentState ->
                currentState.copy(
                    a1Activated =  true,
                    a2Activated = true,
                    a3Activated = false
                )
            }
            nbBrins == 3 -> _uiState.update { currentState ->
                currentState.copy(
                    a1Activated =  true,
                    a2Activated = true,
                    a3Activated = true
                )
            }
            else ->_uiState.update { currentState ->
            currentState.copy(
                a1Activated =  true,
                a2Activated = true,
                a3Activated = true
            )
        }
        }
        updateSecurity()
    }

    private fun updateA1() {
        _uiState.update { currentState ->
            currentState.copy(
                a1 = _uiState.value.rf / _uiState.value.crBrins
            )
        }
    }

    private fun updateSecurity() {
        val security = ((_uiState.value.emd * _uiState.value.crBrins - _uiState.value.rf)/_uiState.value.rf) * 100
        _uiState.update { currentState ->
            currentState.copy(
                safety =  security
            )
        }
        updateA1()
    }

}