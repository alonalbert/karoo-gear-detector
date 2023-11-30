package com.alonalbert.karoogeardetector.module

import io.hammerhead.sdk.v0.datatype.formatter.SdkFormatter

internal class DetectedGearFormatter : SdkFormatter() {
  override fun formatValue(value: Double): String {
    val intValue = value.toInt()
    return "%02d-%02d".format(intValue / 100, intValue % 100)
  }
}
