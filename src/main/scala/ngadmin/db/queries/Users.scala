package ngadmin
package db
package queries

import AppPgDriver.api._
import java.time.ZonedDateTime
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scaldi.Injectable._
import scaldi._
import schema.{UpdateUser,UsersTable,CreateUser,User}

class Users(implicit inj: Injector) {
  val db = inject[Database]
  implicit val ec = inject[ExecutionContext]

  /**
    Create the user given the CreateUser request. Returns created User ID. 
    */
  def createUser(user: CreateUser): Future[Int] = {
    val u = User(
      id = 0,
      email = user.email,
      first_name = user.first_name,
      last_name = user.last_name,
      password_hash = lib.PasswordHasher.createHash(user.password),
      last_login = None,
      created_at = ZonedDateTime.now())

    db.run {
      (UsersTable.query returning (UsersTable.query.map(_.id))) += u
    }
  }

  def update(userId: Int, user: UpdateUser): Future[Int] = {
    db.run {
      UsersTable.forId(userId).
        map(_.updateUser).
        update(user)
    }
  }

  def delete(userId: Int): Future[Int] =
    db.run {
      UsersTable.forId(userId).delete
    }

  def get(userId: Int): Future[Option[User]] =
    db.run {
      UsersTable.forId(userId).result.headOption
    }

  def list(): Future[Seq[User]] =
    db.run {
      UsersTable.query.result
    }
}
