package io.gatling.hani.kafka

import io.gatling.core.action.builder.ActionBuilder
import io.gatling.hani.kafka.request.{KafkaRequestBuilderBase, KafkaSendRequestBuilder}

trait KafkaDsl {
  def kafka(requestName: String) = KafkaRequestBuilderBase(requestName)
  
  implicit def kafkaRequestBuilder2ActionBuilder[K, V](builder: KafkaSendRequestBuilder[K, V]): ActionBuilder = builder.build()
}
