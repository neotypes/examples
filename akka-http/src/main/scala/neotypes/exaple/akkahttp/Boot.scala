package neotypes.exaple.akkahttp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.neo4j.driver.v1.{AuthTokens, GraphDatabase}
import neotypes.implicits._

import scala.concurrent.{ExecutionContext, Future}

object Boot extends App {

  def startApplication() = {
    implicit val actorSystem = ActorSystem()
    implicit val executor: ExecutionContext = actorSystem.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val config = Config.load()

    val driver = GraphDatabase.driver(config.database.url, AuthTokens.basic(config.database.username, config.database.password))

    val movieService = new MovieService(new Driver[Future](driver))
    val httpRoute = new MovieRoute(movieService)

    Http().bindAndHandle(httpRoute.route, config.http.host, config.http.port)
  }

  startApplication()

}
