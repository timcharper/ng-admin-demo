package ngadmin
package db
package schema

import java.time.ZonedDateTime
import AppPgDriver.api._

case class User(
  id: Int,
  email: String,
  first_name: String,
  last_name: String,
  password_hash: String,
  last_login: Option[ZonedDateTime],
  created_at: ZonedDateTime
) {
  def toReadUser =
    ReadUser(
      id         = id,
      email      = email,
      first_name = first_name,
      last_name  = last_name,
      last_login = last_login,
      created_at = created_at)
}

case class CreateUser(
  email: String,
  first_name: String,
  last_name: String,
  password: String)

case class UpdateUser(
  email: String,
  first_name: String,
  last_name: String)

case class ReadUser(
  id: Int,
  email: String,
  first_name: String,
  last_name: String,
  last_login: Option[ZonedDateTime],
  created_at: ZonedDateTime)

class UsersTable(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def email = column[String]("email")
  def first_name = column[String]("first_name")
  def last_name = column[String]("last_name")
  def password_hash = column[String]("password_hash")
  def last_login = column[Option[ZonedDateTime]]("last_login")
  def created_at = column[ZonedDateTime]("created_at")

  def * = (id, email, first_name, last_name, password_hash, last_login, created_at) <> (User.tupled, User.unapply)
  def readUser = (id, email, first_name, last_name, last_login, created_at) <> (ReadUser.tupled, ReadUser.unapply)
  def updateUser = (email, first_name, last_name) <> (UpdateUser.tupled, UpdateUser.unapply)
}

object UsersTable {
  val query = TableQuery[UsersTable]
  def forId(id: Int) = query.filter(_.id === id)
}
