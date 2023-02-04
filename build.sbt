name := "Rithica"

version := "2.9"

scalaVersion := "2.13.2"

fork in run := true

javaOptions in run ++= Seq("-Xmx1024m", "-Xss1m", "-XX:PermSize=32m", "-XX:MaxPermSize=512m", "-XX:+UseConcMarkSweepGC", "-XX:+CMSClassUnloadingEnabled", "-XX:+CMSPermGenSweepingEnabled")

fork in Test := true

javaOptions in Test ++= Seq("-Xmx1024m", "-Xss1m", "-XX:PermSize=32m", "-XX:MaxPermSize=512m", "-XX:+UseConcMarkSweepGC", "-XX:+CMSClassUnloadingEnabled", "-XX:+CMSPermGenSweepingEnabled")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-s")

// parallelExecution in Test := false

autoScalaLibrary := true

publishMavenStyle in ThisBuild := false    
