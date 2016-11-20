name := "proxmock"

version := "0.0.1.ALPHA-SNAPSHOT"

scalaVersion := "2.11.8"

val springVersion = "1.3.5.RELEASE"


libraryDependencies +=  "org.scalafx" %% "scalafx" % "8.0.92-R10"

libraryDependencies ++= Seq(
  "io.reactivex" % "rxscala_2.11" % "0.26.1",
  "org.springframework.boot" % "spring-boot-starter" % springVersion,
  "org.springframework.boot" % "spring-boot-autoconfigure" % springVersion,
  "org.springframework.boot" % "spring-boot-starter-web" % springVersion,
  "org.springframework.boot" % "spring-boot-starter-tomcat" % springVersion,
  "org.springframework.boot" % "spring-boot-starter-integration" % springVersion,
  "org.springframework.integration" % "spring-integration-java-dsl" % "1.1.2.RELEASE",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.7.3",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.7.4",
  "org.webjars" % "bootstrap" % "3.1.1",
  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4",
  "org.scalactic" %% "scalactic" % "2.2.6",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  //
  "org.springframework.boot" % "spring-boot-starter-test" % springVersion % "test",
  "com.jayway.restassured" % "rest-assured" % "2.9.0" % "test"
)
