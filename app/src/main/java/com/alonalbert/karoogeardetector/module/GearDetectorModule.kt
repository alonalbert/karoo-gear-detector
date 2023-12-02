package com.alonalbert.karoogeardetector.module

import com.alonalbert.karoogeardetector.BicycleConfiguration
import com.alonalbert.karoogeardetector.getConfiguration
import io.hammerhead.sdk.v0.Module
import io.hammerhead.sdk.v0.ModuleFactoryI
import io.hammerhead.sdk.v0.SdkContext
import io.hammerhead.sdk.v0.datatype.SdkDataType

@Suppress("unused")
internal class GearDetectorModule(
  context: SdkContext,
  private val getConfiguration: () -> BicycleConfiguration = { context.getConfiguration() },
) : Module(context) {
  override val name = "Gear Detector"
  override val version = "0.1"

  override fun provideDataTypes(): List<SdkDataType> = listOf(
    DetectedGearDataType(context, getConfiguration),
    GearRatioDataType(context, getConfiguration),
  )

  companion object {
    @JvmField
    val factory = object : ModuleFactoryI {
      override fun buildModule(context: SdkContext): Module {
        return GearDetectorModule(context)
      }
    }
  }

}