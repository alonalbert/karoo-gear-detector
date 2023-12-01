package com.alonalbert.karoogeardetector

import kotlin.math.abs

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
