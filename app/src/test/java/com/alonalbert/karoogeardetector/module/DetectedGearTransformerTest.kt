package com.alonalbert.karoogeardetector.module

import com.google.common.truth.Truth.assertThat
import io.hammerhead.sdk.v0.SdkContext
import io.hammerhead.sdk.v0.datatype.Dependency.CADENCE
import io.hammerhead.sdk.v0.datatype.Dependency.SPEED
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.TimeUnit.HOURS
import kotlin.math.abs

internal class DetectedGearTransformerTest {
  private val sdkContext =
    SdkContext.buildModuleContext(
      "package",
      baseContext = mockk(),
      moduleResources = mockk(),
      ClassLoader.getSystemClassLoader()
    )

  @Test
  fun onDependencyChange() {
    val transformer = DetectedGearTransformer(sdkContext)

    testData.forEach {
      it.forEach { list ->
        list.forEach { speedInfo ->
          val ratio = transformer.onDependencyChange(timeStamp = 0, speedInfo.toDependencies())
          val expectedRatio = speedInfo.front.toDouble() / speedInfo.rear

          assertThat(abs((ratio / expectedRatio) - 1)).isLessThan(0.001)
        }
      }
    }
  }

  @Test
  fun onDependencyChange_formatted() {
    val transformer = DetectedGearTransformer(sdkContext)

    testData.forEach {
      it.forEachIndexed { front, list ->
        list.forEachIndexed { rear, speedInfo ->
          val value = transformer.onDependencyChange(timeStamp = 0, speedInfo.toDependencies())
          val formatted = DetectedGearFormatter.formatValue(value)
          assertThat(formatted).isEqualTo("%02d-%02d".format(front + 1, rear + 1))
        }
      }
    }
  }

  private class SpeedInfo(val rpm: Int, val speed: Double, val front: Int, val rear: Int)

  private fun SpeedInfo.toDependencies() =
    mapOf(SPEED to speed.kphToMps(), CADENCE to rpm.toDouble())

  companion object {
    /**
     * Testing data obtained from:
     *
     * https://cyclingroad.com/bicycle-gear-ratio-cadence-and-speed-calculator/
     *
     * Wheel 622mm, Tire 23mm
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
      // 70 RPM
      listOf(
        // Front 34
        listOf(
          SpeedInfo(70, 27.24, 34, 11),
          SpeedInfo(70, 24.97, 34, 12),
          SpeedInfo(70, 23.05, 34, 13),
          SpeedInfo(70, 21.41, 34, 14),
          SpeedInfo(70, 19.98, 34, 15),
          SpeedInfo(70, 17.63, 34, 17),
          SpeedInfo(70, 15.77, 34, 19),
          SpeedInfo(70, 14.27, 34, 21),
          SpeedInfo(70, 12.49, 34, 24),
          SpeedInfo(70, 10.70, 34, 28),
        ),
        // Front 50
        listOf(
          SpeedInfo(70, 40.06, 50, 11),
          SpeedInfo(70, 36.73, 50, 12),
          SpeedInfo(70, 33.90, 50, 13),
          SpeedInfo(70, 31.48, 50, 14),
          SpeedInfo(70, 29.38, 50, 15),
          SpeedInfo(70, 25.92, 50, 17),
          SpeedInfo(70, 23.19, 50, 19),
          SpeedInfo(70, 20.99, 50, 21),
          SpeedInfo(70, 18.36, 50, 24),
          SpeedInfo(70, 15.74, 50, 28),
        ),
      ),
      // 90 RPM
      listOf(
        listOf(
          // Front 34
          SpeedInfo(90, 35.03, 34, 11),
          SpeedInfo(90, 32.11, 34, 12),
          SpeedInfo(90, 29.64, 34, 13),
          SpeedInfo(90, 27.52, 34, 14),
          SpeedInfo(90, 25.69, 34, 15),
          SpeedInfo(90, 22.66, 34, 17),
          SpeedInfo(90, 20.28, 34, 19),
          SpeedInfo(90, 18.35, 34, 21),
          SpeedInfo(90, 16.05, 34, 24),
          SpeedInfo(90, 13.76, 34, 28),
        ),
        listOf(
          // Front 50
          SpeedInfo(90, 51.51, 50, 11),
          SpeedInfo(90, 47.22, 50, 12),
          SpeedInfo(90, 43.59, 50, 13),
          SpeedInfo(90, 40.47, 50, 14),
          SpeedInfo(90, 37.77, 50, 15),
          SpeedInfo(90, 33.33, 50, 17),
          SpeedInfo(90, 29.82, 50, 19),
          SpeedInfo(90, 26.98, 50, 21),
          SpeedInfo(90, 23.61, 50, 24),
          SpeedInfo(90, 20.24, 50, 28),
        ),
      )
    )
  }
}


private fun Double.kphToMps() = this * 1000 / HOURS.toSeconds(1)