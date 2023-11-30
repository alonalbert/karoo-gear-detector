package com.alonalbert.karoogeardetector.module

import com.alonalbert.karoogeardetector.BicycleConfiguration
import io.hammerhead.sdk.v0.SdkContext
import io.hammerhead.sdk.v0.datatype.Dependency
import io.hammerhead.sdk.v0.datatype.Dependency.CADENCE
import io.hammerhead.sdk.v0.datatype.Dependency.SPEED
import io.hammerhead.sdk.v0.datatype.transformer.SdkTransformer
import timber.log.Timber
import kotlin.math.abs

private val configuration = BicycleConfiguration(
  frontGears = listOf(34, 50),
  rearGears = listOf(11, 12, 13, 14, 15, 17, 19, 21, 24, 28),
  wheelDiameterMm = 622,
  tireSizeMm = 23,
)

internal class DetectedGearTransformer(context: SdkContext) : SdkTransformer(context) {
  private val gearRatios = configuration.rearGears.flatMapIndexed { rearIndex: Int, rearSize: Int ->
    configuration.frontGears.mapIndexed { frontIndex, frontSize ->
      GearRatio(rearIndex, frontIndex, frontSize.toDouble() / rearSize)
    }
  }.sortedBy { it.ratio }


  override fun onDependencyChange(timeStamp: Long, dependencies: Map<Dependency, Double>): Double {
    val speed = dependencies[SPEED]
    if (speed == null || speed == MISSING_VALUE) {
      return MISSING_VALUE
    }
    val cadence = dependencies[CADENCE]
    if (cadence == null || cadence == MISSING_VALUE) {
      return MISSING_VALUE
    }


    Timber.d("Speed: $speed m/s")
    Timber.d("Cadence: $cadence rpm")
    val oneToOneSpeed = configuration.getWheelCircumference() * cadence / 60

    Timber.d("One to one speed: $oneToOneSpeed m/s")

    val ratio = speed / oneToOneSpeed
    Timber.d("Ratio: $oneToOneSpeed")
    val gear = gearRatios.findGearFor(ratio) ?: return MISSING_VALUE
    return ((gear.front + 1) * 100 + (gear.rear + 1)).toDouble()
  }

  private data class GearRatio(val rear: Int, val front: Int, val ratio: Double)

  private fun List<GearRatio>.findGearFor(ratio: Double) : GearRatio? {
    var bestMatch: GearRatio? = null
    var bestMatchScore =  Double.MAX_VALUE
    forEach {
      val score = abs(it.ratio - ratio) / it.ratio
      if (score > bestMatchScore) {
        return bestMatch
      }
      bestMatchScore = score
      bestMatch = it
    }
    return bestMatch
  }

}


private fun BicycleConfiguration.getWheelCircumference() =
  (wheelDiameterMm + tireSizeMm * 2) * Math.PI / 1000

fun main() {
  println(configuration.getWheelCircumference() * 60 / 60)
}