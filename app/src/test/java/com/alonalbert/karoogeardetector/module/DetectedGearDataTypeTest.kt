package com.alonalbert.karoogeardetector.module

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class DetectedGearDataTypeTest {

  @Test
  fun onDependencyChange_formatted() {
    val dataType = DetectedGearDataType(TEST_SDK_CONTEXT) { TEST_BICYCLE_CONFIGURATION }
    val transformer = dataType.newTransformer()
    val formatter = dataType.newFormatter()

    TEST_DATA.forEach {
      it.forEachIndexed { front, list ->
        list.forEachIndexed { rear, speedInfo ->
          val value = transformer.onDependencyChange(timeStamp = 0, speedInfo.toDependencies())
          val formatted = formatter.formatValue(value)
          assertThat(formatted).isEqualTo("%02d-%02d".format(front + 1, rear + 1))
        }
      }
    }
  }
}