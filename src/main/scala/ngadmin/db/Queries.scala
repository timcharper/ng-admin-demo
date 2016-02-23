package ngadmin.db

import scaldi._
class Queries(implicit inj: Injector) {
  val users = new queries.Users
  val buckets = new queries.Buckets
}
