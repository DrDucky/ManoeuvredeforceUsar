package com.pomplarg.manoeuvredeforceusar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class VolumeViewModel : ViewModel() {

    private val _volumeState = MutableStateFlow(VolumeState())
    val volumeState: StateFlow<VolumeState> = _volumeState.asStateFlow()
    private val crbrins = mapOf(1 to 1.0, 2 to 1.9, 3 to 2.75, 4 to 3.44, 5 to 4.09)
    val supportItems = mapOf(
        "Béton sec" to Pair(0.8, 0.03),
        "Béton mouillé" to Pair(0.6, 0.03)
    )

    init {
        _volumeState.value = VolumeState()
    }

    fun updateRFManuel(
        weight: Double,
        categoryFrottementRoulementSelected: String,
        supportFrottementRoulementSelected: String,
        angle: Float
    ) {
        updateRF(weight, categoryFrottementRoulementSelected, supportFrottementRoulementSelected, angle)
    }

    fun updateRFCalcul(
        volumeType: String,
        rayon: Double, hauteur: Double, density: Double,
        categoryFrottementRoulementSelected: String,
        supportFrottementRoulementSelected: String,
        angle: Float
    ) {
        val volume = calculateVolume(volumeType, rayon, hauteur)
        val weight = density.let { volume * it } ?: 0.0
        updateRF(weight, categoryFrottementRoulementSelected, supportFrottementRoulementSelected, angle)
    }

    private fun updateRF(
        poidsValue: Double,
        categoryFrottementRoulementSelected: String,
        supportFrottementRoulementSelected: String,
        angle: Float
    ) {
        val cf = when (categoryFrottementRoulementSelected) {
            Constants.FROTTEMENT_KEY -> {
                supportItems[supportFrottementRoulementSelected]?.first
            }

            Constants.ROULEMENT_KEY -> {
                supportItems[supportFrottementRoulementSelected]?.second
            }

            else -> {
                0.0
            }
        }
        cf?.let {
            val rf = when {
                angle > 0 -> poidsValue * (cf * cos(angle.absoluteValue) + sin(angle.absoluteValue))
                angle < 0 -> poidsValue * (cf * cos(angle.absoluteValue) - sin(angle.absoluteValue))
                else -> poidsValue * cf
            }
            _volumeState.update { it.copy(weight = poidsValue, rf = rf) }
            updateNbBrins(rf, _volumeState.value.emd)
        }
        updateSecurity()
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
        val security =
            ((_volumeState.value.emd * _volumeState.value.crBrins - _volumeState.value.rf) / _volumeState.value.rf) * 100
        _volumeState.update { it.copy(safety = security) }
        updateA1()
    }

    private fun calculateVolume(volumeType: String, rayon: Double, hauteur: Double): Double {
        return when (volumeType) {
            Volume.VolumeType.CONE.name -> PI * rayon.pow(2) * hauteur / 3
            Volume.VolumeType.CYLINDER.name -> PI * rayon.pow(2) * hauteur
            else -> {
                PI * rayon.pow(2) * hauteur / 3
            }
        }
    }
}