package ngadmin.controller

import akka.http.scaladsl.server.Route


trait ControllerLike {
  def route: Route
}
