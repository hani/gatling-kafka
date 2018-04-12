package io.gatling.hani.kafka.action

import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.structure.ScenarioContext
import io.gatling.hani.kafka.protocol.KafkaProtocol
import io.gatling.hani.kafka.request.KafkaAttributes

case class KafkaRequestSendBuilder[K, V](attributes: KafkaAttributes[K, V], config: GatlingConfiguration) extends 
        ActionBuilder {
  override def build(ctx: ScenarioContext, next: Action): Action = {
    import ctx._
    val kafkaComponents = protocolComponentsRegistry.components(KafkaProtocol.KafkaProtocolKey)
    val statsEngine = coreComponents.statsEngine
    new KafkaRequestSend(attributes, kafkaComponents.kafkaProtocol, statsEngine, next)
  }
}
