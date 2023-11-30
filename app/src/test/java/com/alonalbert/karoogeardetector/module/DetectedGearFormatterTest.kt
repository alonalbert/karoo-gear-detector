package com.alonalbert.karoogeardetector.module

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DetectedGearFormatterTest {
  @Test
  fun formatValue() {
    val formatter = DetectedGearFormatter()

    assertThat(formatter.formatValue(0101.0)).isEqualTo("01-01")
    assertThat(formatter.formatValue(0210.0)).isEqualTo("02-10")
  }
}