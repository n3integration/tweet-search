name := """play-talk"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  filters,
  "io.jsonwebtoken" % "jjwt" % "0.6.0",
  "org.twitter4j" % "twitter4j-core" % "4.0.4",
  "com.kenshoo" %% "metrics-play" % "2.4.0_0.4.1"
)

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "bootstrap" % "3.3.6",
  "org.webjars" % "angularjs" % "1.5.0",
  "org.webjars" % "angular-ui-router" % "0.2.18"
)

libraryDependencies ++= Seq(
  "io.swagger" %% "swagger-play2" % "1.5.1" exclude("org.reflections", "reflections"),
  "org.reflections" % "reflections" % "0.9.9" notTransitive (),
  "org.webjars" % "swagger-ui" % "2.1.8-M1"
)

// fork in run := false

enablePlugins(SbtNativePackager)

// Dockerfile Configuration
packageName in Docker := "n3integration/tweetsearch"

version in Docker := "v1"

maintainer:= "n3integration@gmail.com"

dockerBaseImage := "netflixoss/java:8"

dockerExposedPorts := Seq(9000)
