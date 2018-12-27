package neotypes.exaple.akkahttp

import neotypes.Driver
import neotypes.exaple.akkahttp.MovieService.MovieToActor
import neotypes.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MovieService(driver: Driver[Future]) {

  def findMovie(title: String): Future[Option[MovieCast]] = driver.readSession { session =>
    c"""MATCH (movie:Movie {title: $title})
       OPTIONAL MATCH (movie)<-[r]-(person:Person)
       RETURN movie.title as title, collect({name:person.name, job:head(split(lower(type(r)),'_')), role:head(r.roles)}) as cast
       LIMIT 1""".query[Option[MovieCast]].single(session)
  }

  def search(query: String): Future[Seq[Movie]] = driver.readSession { session =>
    c"""MATCH (movie:Movie)
        WHERE lower(movie.title) CONTAINS ${query.toLowerCase}
        RETURN movie""".query[Movie].list(session)
  }

  def graph(limit: Int): Future[Graph] = driver.readSession { session =>
    c"""MATCH (m:Movie)<-[:ACTED_IN]-(a:Person)
    RETURN m.title as movie, collect(a.name) as cast
    LIMIT $limit"""
      .query[MovieToActor]
      .list(session)
      .map { result =>
        val nodes = result.flatMap(toNodes)

        val map = nodes.zipWithIndex.toMap
        val rels = result.flatMap { mta =>
          val movie = map(Node(mta.movie, MovieService.LABEL_MOVIE))
          mta.cast.map(c => Relation(movie, map(Node(c, MovieService.LABEL_ACTOR))))
        }

        Graph(nodes, rels)
      }
  }

  private[this] def toNodes(movieToActor: MovieToActor): Seq[Node] =
    movieToActor.cast.map(c => Node(c, MovieService.LABEL_ACTOR)) :+ Node(movieToActor.movie, MovieService.LABEL_MOVIE)
}

object MovieService {
  val LABEL_ACTOR = "actor"
  val LABEL_MOVIE = "movie"

  case class MovieToActor(movie: String, cast: List[String])

}
