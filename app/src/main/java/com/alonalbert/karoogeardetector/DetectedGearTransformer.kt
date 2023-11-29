package com.alonalbert.karoogeardetector

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
    val speed = dependencies[SPEED] ?: return 0.0
    val cadence = dependencies[CADENCE] ?: return 0.0

    Timber.d("Speed: $speed m/s")
    Timber.d("Cadence: $cadence rpm")
    val oneToOneSpeed = configuration.getWheelCircumference() * cadence * 60

    Timber.d("One to one speed: $oneToOneSpeed m/s")

    val ratio = speed / oneToOneSpeed
    Timber.d("Ratio: $oneToOneSpeed")

    return ratio
  }
}

private fun BicycleConfiguration.getWheelCircumference() =
  wheelDiameterMm + tireSizeMm * 2 * Math.PI