package ngadmin

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import scala.concurrent.{ExecutionContext, Future}
import scaldi.Injectable._
import scaldi.Injector

class HttpService(implicit inj:Injector) {
  implicit val system = inject[ActorSystem]
  implicit val materializer = ActorMaterializer()
  val config = ConfigFactory.load()

  val usersController = inject[controller.UsersController]
  val bucketsController = inject[controller.BucketsController]

  val route =
    pathPrefix("v1") {
      pathPrefix("users") {
        usersController.route
      } ~
      pathPrefix("buckets") {
        bucketsController.route
      }
    } ~
    pathPrefix("ui") {
      pathSingleSlash {
        getFromFile("src/main/js/ui/index.html")
      } ~
      getFromDirectory("src/main/js/ui")
    }

  def run() = {
    Http().bindAndHandle(
      route,
      config.getString("server.address"),
      config.getInt("server.port") /*,
         httpsContext = HttpsContextFactory.fromConfig(
           config.getConfig("server.ssl")) */
    )
  }
}
