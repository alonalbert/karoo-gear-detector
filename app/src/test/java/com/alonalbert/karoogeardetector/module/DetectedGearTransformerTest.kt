package com.alonalbert.karoogeardetector.module

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.math.abs

internal class DetectedGearTransformerTest {

  @Test
  fun onDependencyChange() {
    val transformer = DetectedGearTransformer(TEST_SDK_CONTEXT) { TEST_BICYCLE_CONFIGURATION }

    TEST_DATA.forEach {
      it.forEach { list ->
        list.forEach { speedInfo ->
          val ratio = transformer.onDependencyChange(timeStamp = 0, speedInfo.toDependencies())
          val expectedRatio = speedInfo.front.toDouble() / speedInfo.rear

          assertThat(abs((ratio / expectedRatio) - 1)).isLessThan(0.001)
        }
      }
    }
  }
}