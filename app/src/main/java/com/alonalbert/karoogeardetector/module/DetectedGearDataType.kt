package com.alonalbert.karoogeardetector.module

import io.hammerhead.sdk.v0.SdkContext
import io.hammerhead.sdk.v0.datatype.Dependency.CADENCE
import io.hammerhead.sdk.v0.datatype.Dependency.SPEED
import io.hammerhead.sdk.v0.datatype.SdkDataType
import io.hammerhead.sdk.v0.datatype.view.BuiltInView
import kotlin.random.Random

internal class DetectedGearDataType(context: SdkContext) : SdkDataType(context) {
  override val displayName = "Detected Gear"
  override val description = "Derive gear from Speed & Cadence sensors"
  override val typeId = "detected-gear"
  override val dependencies = listOf(SPEED, CADENCE)

  private val random = Random(System.currentTimeMillis())

  override val sampleValue: Double
    get() = (random.nextInt(1, 3) * 100 + random.nextInt(1, 11)).toDouble()

  override fun newFormatter() = DetectedGearFormatter()

  override fun newTransformer() = DetectedGearTransformer(context)

  override fun newView() = BuiltInView.Numeric(context)
}