name := "dispatch-json4s-jackson"

description :=
  "Dispatch module providing json4s support"

Seq(lsSettings :_*)

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-jackson" % "3.2.10",
  "net.databinder" %% "unfiltered-netty" % "0.8.0" % "test"
)

