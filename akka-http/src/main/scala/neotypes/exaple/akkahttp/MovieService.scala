package neotypes.exaple.akkahttp

import neotypes.Driver
import neotypes.exaple.akkahttp.MovieService.MovieToActor
import neotypes.implicits.syntax.string._
import neotypes.generic.auto._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import Boot.Id

class MovieService(driver: Id[Driver[Future]]) {

  def findMovie(title: String): Future[Option[MovieCast]] =
    for {
      r <- s"""MATCH (movie:Movie {title: '$title'})
       OPTIONAL MATCH (movie)<-[r]-(person:Person)
       RETURN movie.title as title, collect({name:person.name, job:head(split(toLower(type(r)),'_')), role:head(r.roles)}) as cast
       LIMIT 1""".query[Option[MovieCast]].single(driver)
    } yield r

  def search(query: String): Future[Seq[Movie]] =
    for {
      r <- s"""MATCH (movie:Movie) WHERE toLower(movie.title) CONTAINS '${query.toLowerCase}' RETURN movie"""
        .query[Movie]
        .list(driver)
    } yield r

  def graph(limit: Int = 100): Future[Graph] = {

    val t: Future[List[MovieToActor]] = for {
      r <- s"""MATCH (m:Movie)<-[:ACTED_IN]-(a:Person) RETURN m.title as movie, collect(a.name) as cast LIMIT $limit"""
        .query[MovieToActor]
        .list(driver)
    } yield r

    t.map { result =>
      val nodes = result.flatMap(toNodes)
      val map: Map[Node, Int] = nodes.zipWithIndex.toMap
      val rels = result.flatMap { mta =>
        val movie = map(Node(mta.movie, MovieService.LABEL_MOVIE))
        mta.cast.map(
          c =>
            Relation(
              source = movie,
              target = map(Node(c, MovieService.LABEL_ACTOR))
          )
        )
      }

      Graph(nodes, rels)
    }
  }

  private[this] def toNodes(movieToActor: MovieToActor): Seq[Node] =
    movieToActor.cast.map(c => Node(c, MovieService.LABEL_ACTOR)) :+ Node(
      movieToActor.movie,
      MovieService.LABEL_MOVIE
    )
}

object MovieService {
  val LABEL_ACTOR = "actor"
  val LABEL_MOVIE = "movie"

  case class MovieToActor(movie: String, cast: List[String])

}
