package com.alonalbert.karoogeardetector.module

import com.google.common.truth.Truth.assertThat
import io.hammerhead.sdk.v0.SdkContext
import io.hammerhead.sdk.v0.datatype.Dependency.CADENCE
import io.hammerhead.sdk.v0.datatype.Dependency.SPEED
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.TimeUnit.HOURS

internal class DetectedGearTransformerTest {
  private val sdkContext =
    SdkContext.buildModuleContext("package", baseContext = mockk(), moduleResources = mockk(), ClassLoader.getSystemClassLoader())

  @Test
  fun onDependencyChange() {
    val transformer = DetectedGearTransformer(sdkContext)

    testData.forEach {
      val value = transformer.onDependencyChange(timeStamp = 0, mapOf(SPEED to it.speed, CADENCE to it.rpm))
      assertThat(value).isEqualTo(it.front * 100 + it.rear)
    }
  }

  private class SpeedInfo(rpm: Int, speed: Double, val front: Int, val rear: Int) {
    val rpm = rpm.toDouble()
    val speed = speed.kphToMps()
  }

  companion object {
    /**
     * Testing data obtained from:
     *
     * https://cyclingroad.com/bicycle-gear-ratio-cadence-and-speed-calculator/
     *
     * ```
     * At 70 RPM:
     * Rear  Big Ring Speed (50) km/h  Little Ring Speed (34) km/h
     * 11	   40.06	                   27.24
     * 12	   36.73	                   24.97
     * 13	   33.90	                   23.05
     * 14	   31.48	                   21.41
     * 15	   29.38	                   19.98
     * 17	   25.92	                   17.63
     * 19	   23.19	                   15.77
     * 21	   20.99	                   14.27
     * 24	   18.36	                   12.49
     * 28	   15.74	                   10.70
     *  ```
     *
     * ```
     * At 90 RPM:
     * Rear  Big Ring Speed (50) km/h  Little Ring Speed (34) km/h
     * 11	   51.51	                   35.03
     * 12	   47.22	                   32.11
     * 13	   43.59	                   29.64
     * 14	   40.47	                   27.52
     * 15	   37.77	                   25.69
     * 17	   33.33	                   22.66
     * 19	   29.82	                   20.28
     * 21	   26.98	                   18.35
     * 24	   23.61	                   16.05
     * 28	   20.24	                   13.76
     *  ```
     */
    private val testData = listOf(
      // Front 50
      SpeedInfo(70, 40.06, 2, 1),
      SpeedInfo(70, 36.73, 2, 2),
      SpeedInfo(70, 33.90, 2, 3),
      SpeedInfo(70, 31.48, 2, 4),
      SpeedInfo(70, 29.38, 2, 5),
      SpeedInfo(70, 25.92, 2, 6),
      SpeedInfo(70, 23.19, 2, 7),
      SpeedInfo(70, 20.99, 2, 8),
      SpeedInfo(70, 18.36, 2, 9),
      SpeedInfo(70, 15.74, 2, 10),

      // Front 34
      SpeedInfo(70, 27.24, 1,  1),
      SpeedInfo(70, 24.97, 1,  2),
      SpeedInfo(70, 23.05, 1,  3),
      SpeedInfo(70, 21.41, 1,  4),
      SpeedInfo(70, 19.98, 1,  5),
      SpeedInfo(70, 17.63, 1,  6),
      SpeedInfo(70, 15.77, 1,  7),
      SpeedInfo(70, 14.27, 1,  8),
      SpeedInfo(70, 12.49, 1,  9),
      SpeedInfo(70, 10.70, 1,  10),

      // Front 50
      SpeedInfo(90, 51.51, 2, 1),
      SpeedInfo(90, 47.22, 2, 2),
      SpeedInfo(90, 43.59, 2, 3),
      SpeedInfo(90, 40.47, 2, 4),
      SpeedInfo(90, 37.77, 2, 5),
      SpeedInfo(90, 33.33, 2, 6),
      SpeedInfo(90, 29.82, 2, 7),
      SpeedInfo(90, 26.98, 2, 8),
      SpeedInfo(90, 23.61, 2, 9),
      SpeedInfo(90, 20.24, 2, 10),

      // Front 34
      SpeedInfo(90, 35.03, 1,  1),      
      SpeedInfo(90, 32.11, 1,  2),      
      SpeedInfo(90, 29.64, 1,  3),      
      SpeedInfo(90, 27.52, 1,  4),      
      SpeedInfo(90, 25.69, 1,  5),      
      SpeedInfo(90, 22.66, 1,  6),      
      SpeedInfo(90, 20.28, 1,  7),      
      SpeedInfo(90, 18.35, 1,  8),      
      SpeedInfo(90, 16.05, 1,  9),      
      SpeedInfo(90, 13.76, 1,  10),     
    )
  }
}


private fun Double.kphToMps() = this * 1000 / HOURS.toSeconds(1)