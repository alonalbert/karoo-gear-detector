package com.alonalbert.karoogeardetector

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class GearDetectorApp : Application() {
  override fun onCreate() {
    super.onCreate()

    Timber.plant(DebugTree())
  }
}