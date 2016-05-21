name := "proxmock"

version := "1.0"

scalaVersion := "2.11.8"

val springVersion = "1.3.5.RELEASE"

//seq(webSettings : _*)

libraryDependencies +=  "org.scalafx" %% "scalafx" % "8.0.92-R10"

libraryDependencies ++= Seq(
  "io.reactivex" % "rxscala_2.11" % "0.26.1",

  "org.springframework.boot" % "spring-boot-starter" % springVersion,
  "org.springframework.boot" % "spring-boot-autoconfigure" % springVersion,
  "org.springframework.boot" % "spring-boot-starter-web" % springVersion,
  "org.springframework.boot" % "spring-boot-starter-tomcat" % springVersion,
  "org.springframework.boot" % "spring-boot-starter-integration" % springVersion,

  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.7.3",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.7.4",

  "org.webjars" % "bootstrap" % "3.1.1",
  "org.webjars" % "jquery" % "2.1.0-2",
  "org.thymeleaf" % "thymeleaf-spring4" % "2.1.2.RELEASE",
  "org.hibernate" % "hibernate-validator" % "5.0.2.Final",
  "nz.net.ultraq.thymeleaf" % "thymeleaf-layout-dialect" % "1.2.1",
  "org.hsqldb" % "hsqldb" % "2.3.1",
  "javax.servlet" % "javax.servlet-api" % "3.0.1" % "provided"
)
//libraryDependencies ++= Seq(
//  "org.apache.tomcat.embed" % "tomcat-embed-core"         % "7.0.53" % "container",
//  "org.apache.tomcat.embed" % "tomcat-embed-logging-juli" % "7.0.53" % "container",
//  "org.apache.tomcat.embed" % "tomcat-embed-jasper"       % "7.0.53" % "container"
//)