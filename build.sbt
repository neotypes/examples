val akkaHttpV = "10.1.3"
val scalaTestV = "3.0.5"
val slickVersion = "3.2.3"
val circeV = "0.9.3"
val sttpV = "1.1.13"
val neotypesV = "0.4.0"

val commonSettings = Seq(
  scalaVersion := "2.12.7"
)

lazy val akkaHttp = (project in file("akka-http"))
  .settings(commonSettings: _*)
  .settings(
    name := "neotypes-cats-effect",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,

      "ch.megard" %% "akka-http-cors" % "0.3.0",
      "com.github.pureconfig" %% "pureconfig" % "0.9.1",

      "io.circe" %% "circe-core" % circeV,
      "io.circe" %% "circe-generic" % circeV,
      "io.circe" %% "circe-parser" % circeV,

      // Sugar for serialization and deserialization in akka-http with circe
      "de.heikoseeberger" %% "akka-http-circe" % "1.20.1",

      "com.dimafeng" %% "neotypes" % neotypesV,
      
      "org.scalatest" %% "scalatest" % scalaTestV % Test
    )
  )
