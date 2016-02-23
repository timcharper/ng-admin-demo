name := "ng-admin-test"

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "SpinGo OSS" at "http://spingo-oss.s3.amazonaws.com/repositories/releases"
)

val playVersion = "2.4.3"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.1.0",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",

  "com.typesafe.akka" %% "akka-http-experimental" % "2.0-M1",
  "com.typesafe.akka" % "akka-stream-experimental_2.11" % "2.0-M1",
  "com.typesafe.akka" %% "akka-actor" % "2.4.0",
  "com.typesafe.play" %% "play-json" % playVersion,
  "org.scaldi" %% "scaldi" % "0.5.6",

  "com.typesafe" % "config" % "1.3.0",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.spingo" %% "scoped-fixtures" % "1.0.0" % "test",
  "com.github.tminglei" %% "slick-pg_date2" % "0.10.1"
)

parallelExecution in Test := false

Revolver.settings
