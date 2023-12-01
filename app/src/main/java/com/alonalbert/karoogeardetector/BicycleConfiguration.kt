package com.alonalbert.karoogeardetector

internal data class BicycleConfiguration(
  val frontGears: List<Int>,
  val rearGears: List<Int>,
  val wheelDiameterMm: Int,
  val tireSizeMm: Int
) {
  val gearRatios =
    rearGears.flatMapIndexed { rearIndex: Int, rearSize: Int ->
      frontGears.mapIndexed { frontIndex, frontSize ->
        GearRatio(rearIndex, frontIndex, frontSize.toDouble() / rearSize)
      }
    }.sortedBy { it.ratio }

  val wheelCircumference = (wheelDiameterMm + tireSizeMm * 2) * Math.PI / 1000

  data class GearRatio(val rear: Int, val front: Int, val ratio: Double)

}
