package scenarios

import io.gatling.core.Predef._
import io.gatling.hani.kafka.Predef._
import io.gatling.hani.kafka.protocol.KafkaProtocol
import org.apache.kafka.clients.producer.ProducerConfig
import scala.concurrent.duration._

class BasicSimulation extends Simulation {
  val protocol = KafkaProtocol(Map(
    ProducerConfig.ACKS_CONFIG -> "1",
    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:19092",

    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG ->
            classOf[org.apache.kafka.common.serialization.LongSerializer].getName,
    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG ->
            classOf[org.apache.kafka.common.serialization.StringSerializer].getName))

  val scn = scenario("Kafka Test")
          .exec(
            kafka("request").send
                    .topic("loadtest")
                    .message[Long, String](System.currentTimeMillis(), System.currentTimeMillis() + "text")
          )

  setUp(scn.inject(constantUsersPerSec(1000) during (60 seconds)))
          .protocols(protocol)
}