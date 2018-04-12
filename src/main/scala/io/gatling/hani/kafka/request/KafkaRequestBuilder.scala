package io.gatling.hani.kafka.request

import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.session.Expression
import io.gatling.hani.kafka.action.KafkaRequestSendBuilder

case class KafkaRequestBuilderBase(requestName: String) {
  def send(implicit config: GatlingConfiguration) = KafkaRequestSendBuilderTopic(requestName, config)
}

case class KafkaRequestSendBuilderTopic(requestName: String, config: GatlingConfiguration) {
  def topic(topic: String) = KafkaRequestSendBuilderMessage(requestName, topic, config)
}

case class KafkaRequestSendBuilderMessage(requestName: String, topic: String, config: GatlingConfiguration) {
  def message[K, V](key: Expression[K], payload: Expression[V]): KafkaSendRequestBuilder[K, V] =
    KafkaSendRequestBuilder[K, V](KafkaAttributes[K, V](requestName, topic, Some(key), payload), 
      KafkaRequestSendBuilder.apply(_, config))

  def message[V](payload: Expression[V]): KafkaSendRequestBuilder[Any, V] =
    KafkaSendRequestBuilder[Any, V](KafkaAttributes[Any, V](requestName, topic, None, payload), 
      KafkaRequestSendBuilder.apply(_, config))
}

case class KafkaSendRequestBuilder[K, V](attributes: KafkaAttributes[K, V], factory: KafkaAttributes[K, V] => 
        ActionBuilder) {
  def build(): ActionBuilder = factory(attributes)
}
