package io.gatling.hani.kafka.request

import io.gatling.core.session.Expression
import io.gatling.hani.kafka.KafkaCheck

case class KafkaAttributes[K, V](
        requestName: String,
        topic: String,
        key: Option[Expression[K]],
        payload: Expression[V],
        checks: List[KafkaCheck[V]] =  Nil
)