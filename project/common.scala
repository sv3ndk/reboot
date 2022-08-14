import sbt._

object Common {
  import Keys._

  val scala3Version = "3.1.3"

  val testSettings:Seq[Setting[_]] = Seq(
    Test / testOptions += Tests.Cleanup { loader =>
      val c = loader.loadClass("unfiltered.spec.Cleanup$")
      c.getMethod("cleanup").invoke(c.getField("MODULE$").get(c))
    },
    Test / testOptions += Tests.Argument(TestFrameworks.ScalaCheck, "-verbosity", "3")
  )

  val settings: Seq[Setting[_]] = Seq(
    version := "1.3.0-SNAPSHOT",

    crossScalaVersions := Seq("2.12.16", "2.13.8", scala3Version),

    scalaVersion := scala3Version,

    // common scalac options
    Compile / scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-language:higherKinds",
      "-language:implicitConversions"
    ),

    // scala version-specifc scalac options
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 12)) =>
          Seq(
            "-Xlint",
            "-Ywarn-dead-code",
            "-Ywarn-numeric-widen",
            "-Ywarn-value-discard"
          )
        case Some((2, 13)) =>
          Seq(
            "-Xlint",
            "-Ywarn-dead-code",
            "-Ywarn-numeric-widen",
            "-Ywarn-value-discard",
            "-Ytasty-reader"
          )
        case Some((3, _)) =>
          Seq(
            "-source",
            "3.0-migration",
          )
        case _ => Nil
      }
    },

    Test / scalacOptions ~= { (opts: Seq[String]) =>
      opts.diff(
        Seq(
          "-Xlint"
        )
      )
    },

    organization := "org.dispatchhttp",

    homepage :=
      Some(new java.net.URL("https://dispatchhttp.org/")),

    publishMavenStyle := true,

    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (version.value.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },

    Test / publishArtifact := false,

    licenses := Seq("LGPL v3" -> url("http://www.gnu.org/licenses/lgpl.txt")),

    pomExtra :=
      <scm>
        <url>git@github.com:dispatch/reboot.git</url>
        <connection>scm:git:git@github.com:dispatch/reboot.git</connection>
      </scm>
      <developers>
        <developer>
          <id>n8han</id>
          <name>Nathan Hamblen</name>
          <url>http://twitter.com/n8han</url>
        </developer>
        <developer>
          <id>farmdawgnation</id>
          <name>Matt Farmer</name>
          <url>https://farmdawgnation.com</url>
        </developer>
      </developers>
  )
}
