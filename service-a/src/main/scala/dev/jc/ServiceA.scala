package dev.jc

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import io.dapr.client.DaprClientBuilder
import io.dapr.client.domain.HttpExtension
import org.slf4j.LoggerFactory
import sttp.tapir._
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

object ServiceA extends App {

  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = actorSystem.dispatcher

  val log = LoggerFactory.getLogger("service-a")

  val dapr = new DaprClientBuilder().build()

  val publishMessages: ServerEndpoint[Any, Future] = endpoint.post
    .in("publish-messages")
    .serverLogicSuccess { _ =>
      dapr
        .publishEvent(
          "messages-pub-sub",
          "messages",
          """{"msg": "hello"}""".getBytes
        )
        .block()
      Future("ok")
    }

  val getMessages: ServerEndpoint[Any, Future] = endpoint.get
    .in("get-messages")
    .serverLogicSuccess { _ =>
      val response = dapr
        .invokeMethod(
          "service-b",
          "get-messages",
          HttpExtension.GET,
          Map.empty[String, String].asJava,
          classOf[Object]
        )
        .block()
      Future(response.toString)
    }

  Http()
    .newServerAt("localhost", 8100)
    .bindFlow(
      AkkaHttpServerInterpreter().toRoute(List(publishMessages, getMessages))
    )
}
