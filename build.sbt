name := """icfp_2015"""

version := "1.0"

scalaVersion := "2.11.7"

// Change this to another test framework if you prefer

//libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.6.4" % "test",
  "com.typesafe.play" %% "play-json" % "2.3.10",
  "com.typesafe.play" %% "play-java-ws" % "2.3.10" % "test",
  "org.scalaz" %% "scalaz-core" % "7.1.3",
  "com.github.scopt" %% "scopt" % "3.3.0"
)
 
// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

retrieveManaged := true

name := "play_icfp2015"

enablePlugins(JavaAppPackaging)