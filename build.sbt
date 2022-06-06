name := "dapr-demo"

version := "0.1"

scalaVersion := "2.13.8"

lazy val serviceA = (project in file("service-a")).settings(commonSettings)
lazy val serviceB = (project in file("service-b")).settings(commonSettings)

lazy val commonSettings = Seq(
  libraryDependencies ++= deps
)

lazy val deps = Seq(
  "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % "1.0.0-RC1",
  "io.dapr" % "dapr-sdk" % "1.5.0",
  "org.slf4j" % "slf4j-api" % "1.7.33",
  "org.slf4j" % "slf4j-simple" % "1.7.33"
)

idePackagePrefix := Some("dev.jc")
