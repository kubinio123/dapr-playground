package dev.jc

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import org.slf4j.LoggerFactory
import sttp.tapir._
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

object ServiceB extends App {

  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = actorSystem.dispatcher

  val log = LoggerFactory.getLogger("service-b")

  val storage = new mutable.MutableList[String]

  val subscribeMessages = endpoint.post
    .in("subscribe-messages")
    .in(byteArrayBody)
    .serverLogicSuccess { bytes =>
      val msg = new String(bytes)
      log.debug(s"Got message: $msg")
      storage += msg
      Future.unit
    }

  val getMessages = endpoint.get
    .in("get-messages")
    .out(stringBody)
    .serverLogicSuccess { _ => Future(s"{${storage.mkString(",\n")}}") }

  Http()
    .newServerAt("localhost", 8200)
    .bindFlow(
      AkkaHttpServerInterpreter().toRoute(List(subscribeMessages, getMessages))
    )
}
