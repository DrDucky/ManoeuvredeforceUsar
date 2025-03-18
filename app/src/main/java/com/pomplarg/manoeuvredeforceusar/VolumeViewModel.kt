package com.pomplarg.manoeuvredeforceusar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class VolumeViewModel : ViewModel() {

    private val _volumeState = MutableStateFlow(VolumeState())
    val volumeState: StateFlow<VolumeState> = _volumeState.asStateFlow()
    private val crbrins = mapOf(1 to 1.0, 2 to 1.9, 3 to 2.71, 4 to 3.44, 5 to 4.09)
    val supportItems = mapOf(
        "Béton sec" to Pair(0.8, 0.03),
        "Béton mouillé" to Pair(0.6, 0.03),
        "Macadam sec" to Pair(0.7, 0.03),
        "Macadam mouillé" to Pair(0.5, 0.06),
        "Terre" to Pair(0.35, 0.3),
        "Boue" to Pair(0.3, 0.25),
        "Roue / rails" to Pair(0.2, 0.005)
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
        volumeType: String?,
        rayon: String?,
        hauteur: String?,
        largeur: String?,
        longueur: String?, density: Double,
        categoryFrottementRoulementSelected: String,
        supportFrottementRoulementSelected: String,
        angle: Float
    ) {
        val volume = calculateVolume(volumeType, rayon, hauteur, largeur, longueur)
        val densityInKg = density * 1000
        val weight = densityInKg.let { volume * it } ?: 0.0
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
            val angleInRadian = Math.toRadians(angle.absoluteValue.toDouble())
            val rf = when {
                angle > 0 -> poidsValue * cf * cos(angleInRadian) + poidsValue * sin(angleInRadian)
                angle < 0 -> poidsValue * cf * cos(angleInRadian) - poidsValue * sin(angleInRadian)
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

    private fun calculateVolume(volumeType: String?, rayon: String?, hauteur: String?, largeur: String?, longueur: String?): Double {

        val r = rayon?.replace(',', '.')?.toDoubleOrNull() ?: 0.0
        val h = hauteur?.replace(',', '.')?.toDoubleOrNull() ?: 0.0
        val l = longueur?.replace(',', '.')?.toDoubleOrNull() ?: 0.0
        val w = largeur?.replace(',', '.')?.toDoubleOrNull() ?: 0.0

        return when (volumeType) {
            Volume.VolumeType.CONE.name -> PI * r.pow(2) * h / 3
            Volume.VolumeType.CYLINDER.name -> PI * r.pow(2) * h
            Volume.VolumeType.PAVE.name -> l * w * h
            Volume.VolumeType.SPHERE.name -> (4 * PI * r.pow(3)) / 3
            else -> 0.0
        }
    }
}