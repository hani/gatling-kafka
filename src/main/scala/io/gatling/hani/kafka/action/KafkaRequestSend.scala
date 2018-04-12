package io.gatling.hani.kafka.action

import io.gatling.commons.stats.{KO, OK, Status}
import io.gatling.commons.util.ClockSingleton.nowMillis
import io.gatling.core.action.{Action, ExitableAction}
import io.gatling.core.session.Session
import io.gatling.core.stats.StatsEngine
import io.gatling.core.stats.message.ResponseTimings
import io.gatling.core.util.NameGen
import io.gatling.hani.kafka.client.KafkaClient
import io.gatling.hani.kafka.protocol.KafkaProtocol
import io.gatling.hani.kafka.request.KafkaAttributes

class KafkaRequestSend[K, V](val attributes: KafkaAttributes[K, V], protocol: KafkaProtocol, val statsEngine: StatsEngine, val next: Action) 
  extends ExitableAction with KafkaAction[K, V] with NameGen {
  
  override val name: String = genName("kafkaSend")
  
  override val client = KafkaClient(protocol, attributes.topic)
  
  override def execute(session: Session): Unit = recover(session) {
      sendMessage(session) {
        case (_, exception, startDate) =>
          // done time
          val endDate = nowMillis
          executeNext(session, startDate, endDate, if(exception == null) OK else KO,
            next,
            attributes.requestName,
            if(exception == null) None else Some(exception.getMessage)
          )
      }
    }
  
  private def executeNext(
    session:  Session,
    sent:     Long,
    received: Long,
    status:   Status,
    next:     Action,
    title:    String,
    message:  Option[String] = None
  ): Unit = {
    val timings = ResponseTimings(sent, received)
    statsEngine.logResponse(session, title, timings, status, None, message)
    next ! session.logGroupRequest(timings.responseTime, status).increaseDrift(nowMillis - received)
  }
}
