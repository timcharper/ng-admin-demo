package ngadmin
package controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import db.queries.BucketSearchParams
import db.schema.{Bucket, CreateBucket}
import scala.concurrent.Future
import scaldi.Injectable._
import scaldi.Injector

class BucketsController(implicit inj:Injector) extends ControllerLike {
  import AppMarshalling._
  import JsonFormats._

  val queries = inject[db.Queries]

  val route = {
    pathEnd {
      parameters('user_id.as[Int].?).as(BucketSearchParams) { searchParams =>
        get {
          complete(
            queries.buckets.search(searchParams))
        }
      } ~
      post {
        entity(as[CreateBucket]) { createBucket =>
          onSuccess(queries.buckets.createBucket(createBucket)) { bucketId =>
            complete(Map("id" -> bucketId))
          }
        }
      }
    } ~
    path(JavaUUID) { bucketId =>
      get {
        onSuccess(queries.buckets.get(bucketId)) {
          case Some(b) =>
            complete(b)
          case None =>
            complete(StatusCodes.NotFound)
        }
      } ~
      delete {
        onSuccess(queries.buckets.delete(bucketId)) { result =>
          complete((StatusCodes.OK, ()))
        }
      }
    }
  }
}

