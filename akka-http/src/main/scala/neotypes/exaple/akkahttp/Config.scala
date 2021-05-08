package neotypes.exaple.akkahttp

import pureconfig.{ConfigSource}
import pureconfig.generic.auto._

case class Config(http: HttpConfig, database: DatabaseConfig)

object Config {
  def load(): Config = ConfigSource.default.load[Config] match {
    case Right(config) => config
    case Left(error) =>
      throw new RuntimeException(
        "Cannot read config file, errors: $error.toList.mkString(\"\\n\")"
      )
  }
}

case class HttpConfig(host: String, port: Int)
case class DatabaseConfig(url: String, username: String, password: String)
