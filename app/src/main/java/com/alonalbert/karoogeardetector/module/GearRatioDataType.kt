package com.alonalbert.karoogeardetector.module

import android.graphics.drawable.Drawable
import com.alonalbert.karoogeardetector.BicycleConfiguration
import com.alonalbert.karoogeardetector.R
import com.alonalbert.karoogeardetector.getConfiguration
import io.hammerhead.sdk.v0.SdkContext
import io.hammerhead.sdk.v0.datatype.Dependency.CADENCE
import io.hammerhead.sdk.v0.datatype.Dependency.SPEED
import io.hammerhead.sdk.v0.datatype.SdkDataType
import io.hammerhead.sdk.v0.datatype.formatter.BuiltInFormatter
import io.hammerhead.sdk.v0.datatype.view.BuiltInView
import kotlin.random.Random

internal class GearRatioDataType(
  context: SdkContext,
  private val getConfiguration: () -> BicycleConfiguration,
) : SdkDataType(context) {
  override val displayName = "Gear Ratio"
  override val description = "Gear ratio from Speed & Cadence sensors"
  override val typeId = "gear-ratio"
  override val dependencies = listOf(SPEED, CADENCE)

  override fun displayIcons(): List<Drawable> =
    listOf(context.getDrawable(R.drawable.gear) ?: throw Exception("Resource not found"))

  private val random = Random(System.currentTimeMillis())

  override val sampleValue: Double
    get() {
      val configuration = context.getConfiguration()
      val frontGear = random.nextItem(configuration.frontGears)
      val rearGear = random.nextItem(configuration.rearGears)
      return frontGear.toDouble() / rearGear
    }

  override fun newTransformer() = DetectedGearTransformer(context, getConfiguration)

  override fun newFormatter() = BuiltInFormatter.Numeric(2)

  override fun newView() = BuiltInView.Numeric(context)
}

private fun <T> Random.nextItem(list: List<T>) = list[nextInt(0, list.size)]