package ngadmin
package db
package queries

import AppPgDriver.api._
import java.time.ZonedDateTime
import java.util.UUID
import schema.BucketsTable
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scaldi.Injectable._
import scaldi._
import schema.{BucketsTable,Bucket,CreateBucket}
import schema.User

case class BucketSearchParams(user_id: Option[Int])

class Buckets(implicit inj: Injector) {
  val db = inject[Database]
  implicit val ec = inject[ExecutionContext]

  val insertQuery = BucketsTable.query returning (BucketsTable.query.map(_.id))
  def createBucket(bucket: CreateBucket): Future[UUID] = {
    val b = Bucket(
      id = UUID.randomUUID(),
      created = ZonedDateTime.now(),
      modified = ZonedDateTime.now(),
      name = bucket.name,
      user_id = bucket.user_id
    )

    db.run {
      insertQuery += b
    }
  }

  def get(bucketId: UUID): Future[Option[Bucket]] = {
    db.run {
      BucketsTable.forId(bucketId).result.headOption
    }
  }

  def search(params: BucketSearchParams): Future[Seq[Bucket]] = db.run {
    /* If multiple BucketSearchParams were implemented, we could emit
     * a series of fragments for which the parameters are defined, and
     * then fold them into a query.
     * 
     * In this case, there is only one param. Just toying with the idea. */
    val filters = List[Option[BucketsTable => Rep[Boolean]]](
      params.user_id.map { userId => { row => row.user_id === userId } }
    )
    val filteredQuery = filters.
      flatten.
      foldLeft(BucketsTable.query: Query[BucketsTable,Bucket,Seq]) { (qry, filter) => qry.filter(filter) }
    filteredQuery.result
  }

  def delete(bucketId: UUID): Future[Int] = {
    db.run {
      BucketsTable.forId(bucketId).delete
    }
  }

  private val random = new java.security.SecureRandom()
  private def generateSalt() = {
    val bytes = Array.ofDim[Byte](16)
    random.nextBytes(bytes)
    bytes
  }
}
