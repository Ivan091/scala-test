package org.test
package http4s

import cats.effect._
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import org.http4s.blaze.server._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.{EntityDecoder, HttpApp, HttpRoutes}

object Application extends IOApp {

  case class Tweet(id: Int, message: String)

  val lists = List(
    Tweet(0, "Lorem ipsum 0"),
    Tweet(1, "Lorem ipsum 1"),
    Tweet(2, "Lorem ipsum 2"),
    Tweet(3, "Lorem ipsum 3"),
    Tweet(4, "Lorem ipsum 4"),
    Tweet(5, "Lorem ipsum 5"),
  )

  implicit val decoder: EntityDecoder[IO, Tweet] = jsonOf[IO, Tweet]

  val testRoute: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "tweet" / IntVar(id) =>
      lists.find(_.id == id) match {
        case Some(tweet) => Ok(tweet.asJson)
        case None => NotFound(s"Count not find a tweet by (id=$id)")
      }
  }

  val httpApp: HttpApp[IO] = Router("/" -> testRoute).orNotFound

  def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}