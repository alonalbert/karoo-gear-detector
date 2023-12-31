package com.alonalbert.karoogeardetector.module

import android.graphics.drawable.Drawable
import com.alonalbert.karoogeardetector.BicycleConfiguration
import com.alonalbert.karoogeardetector.R
import io.hammerhead.sdk.v0.SdkContext
import io.hammerhead.sdk.v0.datatype.Dependency.CADENCE
import io.hammerhead.sdk.v0.datatype.Dependency.SPEED
import io.hammerhead.sdk.v0.datatype.SdkDataType
import io.hammerhead.sdk.v0.datatype.view.BuiltInView
import kotlin.random.Random

internal class DetectedGearDataType(
  context: SdkContext,
  private val getConfiguration: () -> BicycleConfiguration,
  ) : SdkDataType(context) {
  override val displayName = "Detected Gear"
  override val description = "Derive gear from Speed & Cadence sensors"
  override val typeId = "detected-gear"
  override val dependencies = listOf(SPEED, CADENCE)

  override fun displayIcons(): List<Drawable> = listOf(context.getDrawable(R.drawable.gear) ?: throw Exception("Resource not found"))

  private val random = Random(System.currentTimeMillis())

  override val sampleValue: Double
    get() = (random.nextInt(1, 3) * 100 + random.nextInt(1, 11)).toDouble()

  override fun newTransformer() = DetectedGearTransformer(context, getConfiguration)

  override fun newFormatter() = DetectedGearFormatter(getConfiguration)

  override fun newView() = BuiltInView.Numeric(context)
}