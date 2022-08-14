name := "dispatch-lift-json"

description :=
  "Dispatch module providing lift json support"

scalacOptions += "-Xfatal-warnings"

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  "net.liftweb" %% "lift-json" % "3.4.1" exclude("org.scala-lang.modules", "scala-xml_" + scalaBinaryVersion.value),
  "org.mockito" % "mockito-core" % "3.2.4" % "test"
)

// lift-json has not yet been upgraded to scala 3, and relying on for3_use2.13 is discouraged
// => not migrating dispatch-lift-json to scala 3 for now
crossScalaVersions ~= {
  versions => versions.filter{ ! _.startsWith("3")}
}
