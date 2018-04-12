package io.gatling.hani.kafka.protocol

import akka.actor.ActorSystem
import io.gatling.core.CoreComponents
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{Protocol, ProtocolKey}

object KafkaProtocol {
  val KafkaProtocolKey = new ProtocolKey {

    type Protocol = KafkaProtocol
    type Components = KafkaComponents

    def protocolClass: Class[io.gatling.core.protocol.Protocol] = classOf[KafkaProtocol].asInstanceOf[Class[io.gatling.core.protocol.Protocol]]

    def defaultProtocolValue(configuration: GatlingConfiguration): KafkaProtocol = throw new IllegalStateException("Can't provide a default value for KafkaProtocol")

    def newComponents(system: ActorSystem, coreComponents: CoreComponents): KafkaProtocol => KafkaComponents = {
      kafkaProtocol => {
        val kafkaComponents = KafkaComponents(kafkaProtocol)
        kafkaComponents
      }
    }
  }
}

case class KafkaProtocol(properties: Map[String, AnyRef]) extends Protocol {
  def properties(properties: Map[String, AnyRef]): KafkaProtocol = copy(properties = properties)

  def property(key: String, value: AnyRef): KafkaProtocol = copy(properties = properties + (key -> value))
}
