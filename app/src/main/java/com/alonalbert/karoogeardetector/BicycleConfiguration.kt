package com.alonalbert.karoogeardetector

import com.google.gson.Gson
import io.hammerhead.sdk.v0.SdkContext
import kotlin.math.abs

private const val KEY = "configuration"
private val gson by lazy(LazyThreadSafetyMode.NONE) { Gson() }
private val defaultConfiguration = BicycleConfiguration(
  frontGears = listOf(34, 50),
  rearGears = listOf(11, 12, 13, 14, 15, 17, 19, 21, 24, 28),
  wheelDiameterMm = 622,
  tireSizeMm = 23,
)

internal data class BicycleConfiguration(
  val frontGears: List<Int>,
  val rearGears: List<Int>,
  val wheelDiameterMm: Int,
  val tireSizeMm: Int
) {
  private val gearRatios =
    rearGears.flatMapIndexed { rearIndex: Int, rearSize: Int ->
      frontGears.mapIndexed { frontIndex, frontSize ->
        GearRatio(rearIndex, frontIndex, frontSize.toDouble() / rearSize)
      }
    }.sortedBy { it.ratio }

  val wheelCircumference = (wheelDiameterMm + tireSizeMm * 2) * Math.PI / 1000

  fun findGearFor(ratio: Double): GearRatio? {
    var bestMatch: GearRatio? = null
    var bestMatchScore = Double.MAX_VALUE
    gearRatios.forEach {
      val score = abs(it.ratio - ratio) / it.ratio
      if (score > bestMatchScore) {
        return bestMatch
      }
      bestMatchScore = score
      bestMatch = it
    }
    return bestMatch
  }

  data class GearRatio(val rear: Int, val front: Int, val ratio: Double)
}

internal fun SdkContext.getConfiguration(): BicycleConfiguration {
  val json = keyValueStore.getString(KEY) ?: return defaultConfiguration
  return gson.fromJson(json, BicycleConfiguration::class.java)
}

internal fun SdkContext.putConfiguration(configuration: BicycleConfiguration) {
  keyValueStore.putString(KEY, gson.toJson(configuration))
}
