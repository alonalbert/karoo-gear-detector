package com.alonalbert.karoogeardetector.module

import com.google.common.truth.Truth.assertThat
import io.hammerhead.sdk.v0.datatype.formatter.BuiltInFormatter.Numeric
import org.junit.Test

internal class GearRatioDataTypeTest {

  @Test
  fun newTransformer() {
    val dataType = GearRatioDataType(TEST_SDK_CONTEXT) { TEST_BICYCLE_CONFIGURATION }

    assertThat(dataType.newTransformer()).isInstanceOf(DetectedGearTransformer::class.java)
  }

  @Test
  fun newFormatter() {
    val dataType = GearRatioDataType(TEST_SDK_CONTEXT) { TEST_BICYCLE_CONFIGURATION }
    val formatter = dataType.newFormatter()

    assertThat(formatter).isInstanceOf(Numeric::class.java)
    assertThat(formatter.precision).isEqualTo(2)
  }
}