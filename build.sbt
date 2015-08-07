name := """icfp_2015"""

version := "1.0"

scalaVersion := "2.11.7"

// Change this to another test framework if you prefer
//libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.6.4" % "test",
  "com.typesafe.play" %% "play-json" % "2.3.10",
  "org.scalaz" %% "scalaz-core" % "7.1.3"
)

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

retrieveManaged := true