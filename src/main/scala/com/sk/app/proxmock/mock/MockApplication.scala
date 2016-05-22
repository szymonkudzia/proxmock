package com.sk.app.proxmock.mock

import java.io.File
import java.nio.charset.StandardCharsets._
import java.util.concurrent.TimeUnit

import com.sk.app.proxmock.mock.configuration.MockArguments
import com.sk.app.proxmock.mock.domain.Config
import com.sk.app.proxmock.toolset.network.Udp
import com.sk.app.proxmock.toolset.serialization.Yaml
import org.apache.commons.io.FileUtils._
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.{ComponentScan, Configuration}

/**
 * Created by Szymon on 16.05.2016.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = Array(classOf[MockApplication]))
class MockConfiguration

class MockApplication(config: Config, args: Array[String]) {
  private val udp = new Udp().listen(remoteControlMessageHandler)

  private val configuration: Array[Object] = Array(classOf[MockConfiguration])
  private val context = SpringApplication.run(configuration, MockArguments(config, args))

  def close() = {
    println("Closing MockApplication")
    udp.close()
    SpringApplication.exit(context)
  }

  def remoteControlMessageHandler: (String) => Unit = {
    case m if m.contains("proxmock list") => respondWithName
    case m if m.contains(s"proxmock stop ${config.name}") => close()
    case _ =>
  }

  def respondWithName = {
    println(s"Responding for remote call with name: ${config.name}")
    udp.send(s"proxmock name: ${config.name}")
  }
}


object MockApplication {
  def run(filePath: String, metaArgs: Array[String]) = {
    val content = readFileToString(new File(filePath), UTF_8)

    new MockApplication(Yaml.parse(content, classOf[Config]), metaArgs)
  }

  def listRemote() = {
    println("Listing services...")
    new Udp().send("proxmock list").listen(println)
    TimeUnit.SECONDS.sleep(1)
  }

  def closeRemote(name: String) = {
    println(s"Closing service: $name")
    new Udp().send(s"proxmock stop $name")
  }
}