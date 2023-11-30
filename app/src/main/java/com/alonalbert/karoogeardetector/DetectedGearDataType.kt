package com.alonalbert.karoogeardetector

import io.hammerhead.sdk.v0.SdkContext
import io.hammerhead.sdk.v0.datatype.Dependency.CADENCE
import io.hammerhead.sdk.v0.datatype.Dependency.SPEED
import io.hammerhead.sdk.v0.datatype.SdkDataType
import io.hammerhead.sdk.v0.datatype.formatter.BuiltInFormatter
import io.hammerhead.sdk.v0.datatype.view.BuiltInView

internal class DetectedGearDataType(context: SdkContext) : SdkDataType(context) {
  override val displayName = "Detected Gear"
  override val description = "Derive gear from Speed & Cadence sensors"
  override val typeId = "detected-gear"
  override val dependencies = listOf(SPEED, CADENCE)

  override fun newFormatter() = BuiltInFormatter.Numeric(2)

  override fun newTransformer() = DetectedGearTransformer(context)

  override fun newView() = BuiltInView.Numeric(context)
}