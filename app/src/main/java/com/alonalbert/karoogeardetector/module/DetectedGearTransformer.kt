package com.alonalbert.karoogeardetector.module

import com.alonalbert.karoogeardetector.GearDetectorApp.Companion.configuration
import io.hammerhead.sdk.v0.SdkContext
import io.hammerhead.sdk.v0.datatype.Dependency
import io.hammerhead.sdk.v0.datatype.Dependency.CADENCE
import io.hammerhead.sdk.v0.datatype.Dependency.SPEED
import io.hammerhead.sdk.v0.datatype.transformer.SdkTransformer
import timber.log.Timber

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
    val oneToOneSpeed = configuration.wheelCircumference * cadence / 60

    Timber.d("One to one speed: $oneToOneSpeed m/s")

    val ratio = speed / oneToOneSpeed
    Timber.d("Ratio: $oneToOneSpeed")
    val gear = configuration.findGearFor(ratio) ?: return MISSING_VALUE
    return ((gear.front + 1) * 100 + (gear.rear + 1)).toDouble()
  }
}
