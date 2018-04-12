package io.gatling.hani.kafka.client

import java.util.concurrent.Future

import io.gatling.commons.util.ClockSingleton.nowMillis
import io.gatling.hani.kafka.protocol.KafkaProtocol
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

object KafkaClient {
  def apply[K, V](protocol: KafkaProtocol, topic: String): KafkaSendClient[K, V] = {
    new KafkaSendClient(protocol, topic)
  }
}

class KafkaSendClient[K, V](protocol: KafkaProtocol, topic: String) {

  import scala.collection.JavaConverters._

  val producer = new KafkaProducer[K, V](protocol.properties.asJava)

  def send(key: Option[K], value: V, f: (RecordMetadata, Exception, Long) => Unit): Future[RecordMetadata] = {
    val startDate = nowMillis
    key match {
      case Some(k) =>
        producer.send(new ProducerRecord[K, V](topic, k, value), (metadata: RecordMetadata, exception: Exception) => {
          f(metadata, exception, startDate)
        })
      case None =>
        producer.send(new ProducerRecord[K, V](topic, value), (metadata: RecordMetadata, exception: Exception) => {
          f(metadata, exception, startDate)
        })
    }
  }
}
