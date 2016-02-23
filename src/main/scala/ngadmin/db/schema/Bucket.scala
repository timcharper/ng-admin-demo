package ngadmin.db
package schema
import java.time.ZonedDateTime
import java.util.UUID

import AppPgDriver.api._

case class Bucket(
  id: UUID,
  created: ZonedDateTime,
  modified: ZonedDateTime,
  name: String,
  user_id: Int)

case class CreateBucket(
  name: String,
  user_id: Int
)

class BucketsTable(tag: Tag) extends Table[Bucket](tag, "buckets") {
  val id = column[UUID]("id", O.PrimaryKey)
  val created = column[ZonedDateTime]("created")
  val modified = column[ZonedDateTime]("modified")
  val name = column[String]("name")
  val user_id = column[Int]("user_id")

  val user = foreignKey("bucket_user_fk", user_id, UsersTable.query)(_.id)

  def * = (id, created, modified, name, user_id) <> (Bucket.tupled, Bucket.unapply)
}

object BucketsTable {
  val query = TableQuery[BucketsTable]
  def forId(id: UUID) = query.filter(_.id === id)
}
