ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.1"
Global / excludeLintKeys += idePackagePrefix
//idePackagePrefixGlobal / cancelable := true
//fork / run := true


val Http4sVersion = "1.0.0-M30"
val CirceVersion = "0.14.3"
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,
  "io.circe" %% "circe-generic" % CirceVersion,
  "org.scalatest" %% "scalatest" % "3.2.14" % Test,
  "ch.qos.logback" % "logback-classic" % "1.4.5" % Runtime,
  "org.typelevel" %% "cats-core" % "2.9.0",
  "org.typelevel" %% "mouse" % "1.2.1",
  "org.typelevel" %% "cats-effect" % "3.4.1",
  "org.typelevel" %% "cats-mtl" % "1.3.0",
  "ch.qos.logback" % "logback-classic" % "1.4.5",
)

//noinspection SpellCheckingInspection
scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-deprecation",
  "-source:future",
)

lazy val root = (project in file("."))
  .settings(
    name := "scalaTest",
    idePackagePrefix := Some("org.test"),
  )
