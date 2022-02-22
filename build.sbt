ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / name := "scalaTest"
ThisBuild / scalaVersion := "2.13.8"
Global / excludeLintKeys += idePackagePrefix

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
  "org.typelevel" %% "mouse" % "1.0.9",
  "org.typelevel" %% "cats-effect" % "3.3.5",
  "org.typelevel" %% "cats-mtl" % "1.2.1",
  "org.apache.logging.log4j" %% "log4j-api-scala" % "12.0",
  "org.apache.logging.log4j" % "log4j-core" % "2.17.1",
)

//noinspection SpellCheckingInspection
scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-deprecation"
)

lazy val root = (project in file("."))
  .settings(
    name := "scalaTest",
    idePackagePrefix := Some("org.test"),
  )