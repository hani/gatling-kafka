package io.gatling.hani.kafka.protocol

import io.gatling.core.protocol.ProtocolComponents
import io.gatling.core.session.Session

case class KafkaComponents(kafkaProtocol: KafkaProtocol) extends ProtocolComponents {

  def onStart: Option[Session => Session] = None

  def onExit: Option[Session => Unit] = None
}
