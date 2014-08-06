import sbt._
import sbt.Keys._

import com.typesafe.sbt.SbtScalariform
import xerial.sbt.Sonatype._

object SqlestBuild extends Build {

  lazy val sqlest = Project(
    id = "sqlest",
    base = file("."),
    aggregate = Seq(sqlestCore, sqlestExamples),
    settings = commonSettings ++ Seq(
      moduleName := "sqlest-root",

      publish := (),
      publishLocal := ()
    )
  )

  lazy val sqlestCore = Project(
    id = "sqlest-core",
    base = file("core"),

    settings = commonSettings ++ publishingSettings ++ Seq(
      moduleName := "sqlest",
      version := "0.2.0-SNAPSHOT",

      libraryDependencies ++= Seq(
        "joda-time" % "joda-time" % "2.3",
        "org.joda" % "joda-convert" % "1.6",
        "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
        "org.scalatest" %% "scalatest" % "2.1.7" % "test"
      )
    )
  )

  lazy val sqlestExamples = Project(
    id = "sqlest-examples",
    base = file("examples"),
    dependencies = Seq(sqlestCore),

    settings = commonSettings ++ Seq(
      libraryDependencies += "com.h2database" % "h2" % "1.4.180",

      publish := (),
      publishLocal := ()
    )
  )

  def commonSettings = SbtScalariform.scalariformSettings ++ Seq(
    organization := "uk.co.jhc",
    scalaVersion := "2.11.2",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")
  )

  def publishingSettings = sonatypeSettings ++ Seq(
    // Publishing - http://www.scala-sbt.org/0.13/docs/Using-Sonatype.html
    publishMavenStyle := true,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    credentials := Seq(Seq("SONATYPE_USER", "SONATYPE_PASSWORD").map(key => sys.env.get(key)) match {
      case Seq(Some(user), Some(password)) =>
        Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", user, password)
      case _                           =>
        Credentials(Path.userHome / ".ivy2" / ".credentials")
    }),
    pomIncludeRepository := { _ => false },
    pomExtra := (
      <url>https://github.com/jhc-systems/sqlest</url>
      <licenses>
        <license>
          <name>Apache License, Version 2.0</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com/jhc-systems/sqlest.git</url>
        <connection>scm:git:git@github.com/jhc-systems/sqlest.git</connection>
      </scm>
      <developers>
        <developer>
          <id>davegurnell</id>
          <name>Dave Gurnell</name>
        </developer>
        <developer>
          <id>brendanator</id>
          <name>Brendan Maginnis</name>
        </developer>
      </developers>)
  )
}