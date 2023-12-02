package com.alonalbert.karoogeardetector.module

import com.alonalbert.karoogeardetector.BicycleConfiguration
import io.hammerhead.sdk.v0.datatype.formatter.BuiltInFormatter
import io.hammerhead.sdk.v0.datatype.formatter.SdkFormatter

internal class DetectedGearFormatter(
  private val getConfiguration: () -> BicycleConfiguration,
) : SdkFormatter() {
  override fun formatValue(value: Double): String {
    val gear = getConfiguration().findGearFor(value)
      ?: return BuiltInFormatter.Numeric(1).formatValue(value)
    return "%02d-%02d".format(gear.front + 1, gear.rear + 1)
  }
}
