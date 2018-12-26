package neotypes.exaple.akkahttp

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._
import akka.http.scaladsl.server.directives._
import ContentTypeResolver.Default

import scala.concurrent.ExecutionContext

class MovieRoute(movieService: MovieService)(implicit executionContext: ExecutionContext)
  extends FailFastCirceSupport {

  import StatusCodes._

  val route = pathPrefix("movie") {
    pathPrefix(Segment) { title =>
      pathEndOrSingleSlash {
        get {
          complete(movieService.findMovie(title).map {
            case Some(movie) =>
              OK -> movie.asJson
            case None =>
              BadRequest -> None.asJson
          })
        }
      }
    }
  } ~ pathPrefix("search") {
    parameters('q) { query =>
      get {
        complete(movieService.search(query).map(res => OK -> res.asJson))
      }
    }
  } ~ pathPrefix("graph") {
    pathEndOrSingleSlash {
      get {
        complete(movieService.graph(100).map(res => OK -> res.asJson))
      }
    }
  } ~ path("index.html") {
    get {
      getFromResource("index.html")
    }
  }

}
