package com.alonalbert.karoogeardetector.module

import io.hammerhead.sdk.v0.datatype.Dependency

internal class SpeedInfo(val rpm: Int, val speed: Double, val front: Int, val rear: Int) {
  fun toDependencies() =
    mapOf(Dependency.SPEED to speed.kphToMps(), Dependency.CADENCE to rpm.toDouble())
}