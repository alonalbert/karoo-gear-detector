package com.alonalbert.karoogeardetector

internal data class BicycleConfiguration(
  val frontGears: List<Int>,
  val rearGears: List<Int>,
  val wheelDiameterMm: Int,
  val tireSizeMm: Int
)
