val akkaHttpV = "10.2.4"
val scalaTestV = "3.2.8"
val slickVersion = "3.2.3"
val circeV = "0.13.0"
val sttpV = "1.1.13"
val neotypesV = "0.17.0"

//val commonSettings = Seq(scalaVersion := "2.12.7")
val commonSettings = Seq(scalaVersion := "2.13.5")

lazy val akkaHttp = (project in file("akka-http"))
  .settings(commonSettings: _*)
  .settings(
    name := "neotypes-cats-effect",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,
      "com.typesafe.akka" %% "akka-actor" % "2.6.14",
      "com.typesafe.akka" %% "akka-stream" % "2.6.14",
      "ch.megard" %% "akka-http-cors" % "1.1.1",
      "com.github.pureconfig" %% "pureconfig" % "0.15.0",
      "io.circe" %% "circe-core" % circeV,
      "io.circe" %% "circe-generic" % circeV,
      "io.circe" %% "circe-parser" % circeV,
      // Sugar for serialization and deserialization in akka-http with circe
      "de.heikoseeberger" %% "akka-http-circe" % "1.36.0",
      "com.dimafeng" %% "neotypes" % neotypesV,
      "org.scalatest" %% "scalatest" % scalaTestV % Test,
      "org.neo4j.driver" % "neo4j-java-driver" % "4.2.5"
    )
  )
