package io.gatling.hani

import io.gatling.core.check.Check

package object kafka {
  type KafkaCheck[V] = Check[V]
}
