package com.alonalbert.karoogeardetector

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

internal class GearDetectorApp : Application() {
  override fun onCreate() {
    super.onCreate()

    Timber.plant(DebugTree())
  }

  companion object {
    val configuration = BicycleConfiguration(
      frontGears = listOf(34, 50),
      rearGears = listOf(11, 12, 13, 14, 15, 17, 19, 21, 24, 28),
      wheelDiameterMm = 622,
      tireSizeMm = 23,
    )
  }
}