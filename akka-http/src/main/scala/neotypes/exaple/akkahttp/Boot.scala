package neotypes.exaple.akkahttp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import neotypes.{Driver, GraphDatabase}
import org.neo4j.driver.AuthTokens

import scala.concurrent.{ExecutionContext, Future}

object Boot extends App {

  type Id[A] = A

  def startApplication() = {

    implicit val actorSystem = ActorSystem()
    implicit val executor: ExecutionContext = actorSystem.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val config = Config.load()

    val driver: Id[Driver[Future]] = GraphDatabase.driver[Future](
      config.database.url,
      AuthTokens.basic(config.database.username, config.database.password)
    )

    val movieService = new MovieService(driver)
    val httpRoute = new MovieRoute(movieService)

    Http().bindAndHandle(httpRoute.route, config.http.host, config.http.port)

    Runtime.getRuntime().addShutdownHook(new Thread(() => driver.close))
  }

  startApplication()
}
