lazy val V = _root_.scalafix.sbt.BuildInfo
inThisBuild(
  List(
    scalaVersion := "2.13.2",
    addCompilerPlugin(scalafixSemanticdb("4.3.10")),
    scalacOptions ++= List(
      "-Yrangepos"
    )
  )
)

skip in publish := true

lazy val rules = project.settings(
  moduleName := "autofix",
  version := "3.1.0.2-SNAPSHOT",
  crossScalaVersions := List("2.12.8", "2.11.12"),
  organization := "org.scalatest",
    homepage := Some(url("https://github.com/scalatest/autofix")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "bvenners",
        "Bill Venners",
        "bill@artima.com",
        url("https://github.com/bvenners")
      ),
      Developer(
        "cheeseng",
        "Chua Chee Seng",
        "cheeseng@amaseng.com",
        url("https://github.com/cheeseng")
      )
    ), 
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % "0.9.15.2-SNAPSHOT", 
    credentials += Credentials("Sonatype Nexus Repository Manager", "127.0.0.1", "admin", "admin"),
    publishTo := Some("Sonatype Nexus Repository Manager" at "http://127.0.0.1:8081/repository/maven-snapshots"),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false }
  )

lazy val input = project.settings(
  skip in publish := true,
  libraryDependencies ++=
    Seq(
      "org.scalatest" %% "scalatest" % "3.1.0",
      "org.scalacheck" %% "scalacheck" % "1.14.2",
      "org.easymock" % "easymockclassextension" % "3.2",
      "org.jmock" % "jmock-legacy" % "2.8.3",
      "org.mockito" % "mockito-core" % "1.10.19",
      "org.seleniumhq.selenium" % "selenium-java" % "2.45.0",
      "junit" % "junit" % "4.12",
      "org.testng" % "testng" % "6.7"
    )
)

lazy val output = project.settings(
  skip in publish := true,
  libraryDependencies ++=
    Seq(
      "org.scalatest" %% "scalatest" % "3.1.0",
      "org.scalacheck" %% "scalacheck" % "1.14.2",
      "org.easymock" % "easymockclassextension" % "3.2",
      "org.jmock" % "jmock-legacy" % "2.8.3",
      "org.mockito" % "mockito-core" % "1.10.19",
      "org.seleniumhq.selenium" % "selenium-java" % "2.45.0",
      "junit" % "junit" % "4.12",
      "org.testng" % "testng" % "6.7"
    )
)

lazy val tests = project
  .settings(
    skip in publish := true,
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % "0.9.15.2-SNAPSHOT" % Test cross CrossVersion.full,
    scalafixTestkitOutputSourceDirectories :=
      sourceDirectories.in(output, Compile).value,
    scalafixTestkitInputSourceDirectories :=
      sourceDirectories.in(input, Compile).value,
    scalafixTestkitInputClasspath :=
      fullClasspath.in(input, Compile).value
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)
