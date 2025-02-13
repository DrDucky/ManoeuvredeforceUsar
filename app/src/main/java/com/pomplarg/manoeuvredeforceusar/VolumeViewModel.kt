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

    private val _volumeState = MutableStateFlow(VolumeState())
    val volumeState: StateFlow<VolumeState> = _volumeState.asStateFlow()
    private val _volumeUiState = MutableStateFlow(VolumeUiState())
    val volumeUiState: StateFlow<VolumeUiState> = _volumeUiState.asStateFlow()
    private val crbrins = mapOf(1 to 1.0, 2 to 1.9, 3 to 2.75, 4 to 3.44, 5 to 4.09)

    init {
        _volumeState.value = VolumeState()
        _volumeUiState.value = VolumeUiState()
    }

    fun updateVolume(volumeType: Volume.VolumeType, rayon: Double, hauteur: Double) {
        val volume = calculateVolume(volumeType, rayon, hauteur)
        _volumeState.update { it.copy(volume = volume) }
        updateWeight(volume, _volumeState.value.density)
    }

    fun updateDensity(density: Int?) {
        density?.let {
            _volumeState.update { it.copy(density = density) }
            updateWeight(_volumeState.value.volume, density)
        }
    }

    fun updateRF(cf: Double?) {
        cf?.let {
            val rf = _volumeState.value.weight * cf
            _volumeState.update { it.copy(cf = cf, rf = rf) }
            updateNbBrins(rf, _volumeState.value.emd)
        }
        updateSecurity()
    }

    private fun updateWeight(volume: Double, density: Int?) {
        val weight = density?.let { volume * it } ?: 0.0
        _volumeState.update { it.copy(weight = weight) }
        updateRF(_volumeState.value.cf)
    }

    fun updateWeightWithManualEntry(entry: String) {
        if (entry.isNotBlank()) {
            _volumeState.update { it.copy(weight = entry.toDouble()) }
            updateRF(_volumeState.value.cf)
        }
    }

    fun updateManuelChoice(manualChoice: Boolean) {
        _volumeUiState.update { it.copy(manualChoice = manualChoice) }
    }

    fun updateEmd(emd: Int) {
        _volumeState.update { it.copy(emd = emd) }
        updateNbBrins(_volumeState.value.rf, emd)
        updateSecurity()
    }

    fun updateNbBrins(rf: Double, emd: Int) {
        val nbBrins = (ceil(rf / emd).toInt() + 1).coerceAtLeast(0)
        _volumeState.update { it.copy(nbBrins = nbBrins, crBrins = crbrins[nbBrins] ?: 0.0) }
        updateAStates(nbBrins)
        updateSecurity()
    }

    private fun updateAStates(nbBrins: Int) {
        _volumeState.update {
            it.copy(
                a1Activated = nbBrins >= 1,
                a2Activated = nbBrins >= 2,
                a3Activated = nbBrins >= 3
            )
        }
    }

    private fun updateA1() {
        _volumeState.update { it.copy(a1 = _volumeState.value.rf / _volumeState.value.crBrins) }
    }

    private fun updateSecurity() {
        val security = ((_volumeState.value.emd * _volumeState.value.crBrins - _volumeState.value.rf) / _volumeState.value.rf) * 100
        _volumeState.update { it.copy(safety = security) }
        updateA1()
    }

    fun updateVolumeSelected(openConeDialogSelected: Boolean, openCylinderDialogSelected: Boolean) {
        _volumeUiState.update { it.copy(openConeDialogSelected = openConeDialogSelected,
            openCylinderDialogSelected = openCylinderDialogSelected) }
    }

    private fun calculateVolume(volumeType: Volume.VolumeType, rayon: Double, hauteur: Double): Double {
        return when (volumeType) {
            Volume.VolumeType.CONE -> PI * rayon.pow(2) * hauteur / 3
            Volume.VolumeType.CYLINDER -> PI * rayon.pow(2) * hauteur
        }
    }
}