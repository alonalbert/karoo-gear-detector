package com.alonalbert.karoogeardetector.module

import com.alonalbert.karoogeardetector.BicycleConfiguration
import io.hammerhead.sdk.v0.SdkContext
import io.hammerhead.sdk.v0.datatype.Dependency
import io.hammerhead.sdk.v0.datatype.Dependency.CADENCE
import io.hammerhead.sdk.v0.datatype.Dependency.SPEED
import io.hammerhead.sdk.v0.datatype.transformer.SdkTransformer
import timber.log.Timber

private val configuration = BicycleConfiguration(
  frontGears = listOf(34, 50),
  rearGears = listOf(11, 12, 13, 14, 15, 17, 19, 24, 28),
  wheelDiameterMm = 622,
  tireSizeMm = 23,
)

internal class DetectedGearTransformer(context: SdkContext) : SdkTransformer(context) {
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

    return ratio
  }
}

private fun BicycleConfiguration.getWheelCircumference() =
  (wheelDiameterMm + tireSizeMm * 2) * Math.PI / 1000

fun main() {
  println(configuration.getWheelCircumference() * 60 / 60)
}