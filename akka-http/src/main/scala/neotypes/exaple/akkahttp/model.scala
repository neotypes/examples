package neotypes.exaple.akkahttp

case class Cast(name: String, job: String, role: String)

case class MovieCast(title: String, cast: List[Cast])

case class Movie(title: String, released: Int, tagline: String)

case class Graph(nodes: Seq[Node], links: Seq[Relation])

case class Relation(source: Int, target: Int)

case class Node(title: String, label: String)