package ngadmin

import akka.actor.ActorSystem
import scaldi._
import scaldi.Injectable._
import db.schema
import db.AppPgDriver.api._
import scala.concurrent.ExecutionContext
// import db.BucketsTable
class Universe extends Module {
  private val driver = Class.forName("org.postgresql.Driver") // trigger class load
  bind [ExecutionContext] to ExecutionContext.global
  bind [ActorSystem] to ActorSystem("ngadmin")
  bind [HttpService] to new HttpService
  bind [db.Queries] to new db.Queries
  bind [controller.UsersController] to new controller.UsersController
  bind [controller.BucketsController] to new controller.BucketsController
  bind [Database] to Database.forURL("jdbc:postgresql://localhost:5432/ngadmin",
    Map(
      "driver" -> "org.postgresql.Driver",
      "user" -> "ngadmin",
      "password" -> "ngadmin"))
}

object Boot extends App {
  val module = new Universe()
  import module.injector
  val running = inject[HttpService].run()

  def createSchema: Unit = {
    val db = inject[Database]
    implicit val ec = inject[ExecutionContext]
    db.run(DBIO.seq(schema.UsersTable.query.schema.create)) foreach println
    db.run(DBIO.seq(schema.BucketsTable.query.schema.create)) onComplete println
  }
  createSchema
}
