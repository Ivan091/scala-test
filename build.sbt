ThisBuild / version := "0.1.0-SNAPSHOT"

val Http4sVersion = "1.0.0-M30"
val CirceVersion = "0.14.1"
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,
  "io.circe" %% "circe-generic" % CirceVersion,
  "org.scalatest" %% "scalatest" % "3.2.9" % Test,
  "ch.qos.logback" % "logback-classic" % "1.2.10" % Runtime,
  "org.typelevel" %% "cats-core" % "2.7.0",
  "org.typelevel" %% "mouse" % "1.0.8",

)

scalacOptions ++= Seq(
  "-Xfatal-warnings"
)

ThisBuild / scalaVersion := "2.13.8"
Global / excludeLintKeys += idePackagePrefix

lazy val root = (project in file("."))
  .settings(
    name := "scalat",
    idePackagePrefix := Some("org.test"),
  )