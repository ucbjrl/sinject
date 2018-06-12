import sbt.CrossVersion

organization  := "sinject"

name := "sinject"

version       := "0.1.1"

scalaVersion  := "2.12.6"

scalacOptions ++= Seq(
  "-feature",
  "-language:reflectiveCalls",
  "-deprecation"
)

crossScalaVersions := Seq("2.11.12", "2.12.6")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.scala-lang" % "scala-compiler" % scalaVersion.value
)

unmanagedSourceDirectories in Test += { baseDirectory(_ / "src" / "test" / "resources").value }

// Write the specified contents to the specified file.
def writeBuildFileContent(buildFile: File, content: String): Seq[File] = {
  IO.write(buildFile, content)
  Seq(buildFile)
}

// Generate a test source file containing the relevant definitions to invoke the appropriate compiler during testing.
sourceGenerators in Test += Def.task {
  // Retain only the major, minor version components
  val (major, minor) = CrossVersion.partialVersion(scalaVersion.value).get
  val compilerVersion = "%d.%d".format(major, minor)
  val versionRoot = s"scala-${compilerVersion}"
  val runtimeRoot = java.lang.System.getProperty("scala.runtime.root", "/Runtimes" /* "/opt/local/share" */)
  val scalaLibDir = s"${runtimeRoot}/${versionRoot}/lib"
  // Verify that the compiler is available
  assert((new File(scalaLibDir)).isDirectory, "Please define versionRoot and runtimeRoot so ${runtimeRoot}/${versionRoot}/lib is a directory containing the scala jars: " + scalaLibDir + " doesn't exist")
  val content =
    s"""|package sinject
        |object CompilerInfo {
        |  val compilerVersion = "${compilerVersion}"
        |  val versionRoot = "${versionRoot}"
        |  val runtimeRoot = "${runtimeRoot}"
        |}
     """.stripMargin
  writeBuildFileContent((sourceManaged in Test).value / "CompilerInfo.scala", content)
}
