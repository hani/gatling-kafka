package scenarios

import io.gatling.core.Predef._
import io.gatling.hani.kafka.Predef._
import io.gatling.hani.kafka.protocol.KafkaProtocol
import org.apache.kafka.clients.producer.ProducerConfig
import scala.concurrent.duration._

class BasicSimulation extends Simulation {
  val protocol = KafkaProtocol(Map(
    ProducerConfig.ACKS_CONFIG -> "1",
    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",

    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG ->
            "org.apache.kafka.common.serialization.StringSerializer",
    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG ->
            "org.apache.kafka.common.serialization.StringSerializer"))

  val scn = scenario("Kafka Test")
          .exec(
            kafka("request").send
                    .topic("mytopic")
                    .message[String, String]("foo", "bar")
          )

  setUp(scn.inject(constantUsersPerSec(10) during (90 seconds)))
          .protocols(protocol)
}