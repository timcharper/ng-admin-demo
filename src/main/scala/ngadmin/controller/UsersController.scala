package ngadmin
package controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import db.schema.{CreateUser, ReadUser, UpdateUser, User}
import scala.concurrent.Future
import scaldi.Injectable._
import scaldi.Injector

class UsersController(implicit inj:Injector) extends ControllerLike {
  import AppMarshalling._
  import JsonFormats._

  val queries = inject[db.Queries]

  val route = {
    pathEnd {
      post {
        entity(as[CreateUser]) { userParams =>
          onSuccess(queries.users.createUser(userParams)) { userId =>
            complete(Map("id" -> userId))
          }
        }
      } ~
      get {
        onSuccess(queries.users.list()) { users =>
          complete(
            users.map(_.toReadUser))
        }
      }
    } ~
    path(IntNumber) { userId =>
      get {
        onSuccess(queries.users.get(userId)) {
          case Some(u) =>
            complete(u.toReadUser)
          case None =>
            complete(StatusCodes.NotFound)
        }
      } ~
      put {
        entity(as[UpdateUser]) { updateUser =>
          onSuccess(queries.users.update(userId, updateUser)) { result =>
            complete((StatusCodes.OK, ()))
          }
        }
      } ~
      delete {
        onSuccess(queries.users.delete(userId)) { result =>
          complete((StatusCodes.OK, ()))
        }
      }
    }
  }
}

