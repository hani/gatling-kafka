package io.gatling.hani.kafka.action

import io.gatling.commons.validation.Validation
import io.gatling.core.session.Session
import io.gatling.hani.kafka.client.KafkaSendClient
import io.gatling.hani.kafka.request.KafkaAttributes
import org.apache.kafka.clients.producer.RecordMetadata

trait KafkaAction[K, V] {

  def attributes: KafkaAttributes[K, V]

  def client: KafkaSendClient[K, V]

  def sendMessage(session: Session)(f: (RecordMetadata, Exception, Long) => Unit): Validation[Unit] = {
    attributes.payload(session).map { payload =>
      client.send(attributes.key.map(_ (session)).map(_.get), payload, f)
    }
  }
}
