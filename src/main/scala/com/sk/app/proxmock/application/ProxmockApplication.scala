package com.sk.app.proxmock.application

import java.io.File
import java.nio.charset.StandardCharsets._
import java.util.concurrent.TimeUnit

import com.sk.app.proxmock.application.configuration.SpringArguments
import com.sk.app.proxmock.application.domain.Config
import com.sk.app.proxmock.toolset.network.Udp
import com.sk.app.proxmock.toolset.serialization.Yaml
import org.apache.commons.io.FileUtils._
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}

/**
 * Created by Szymon on 16.05.2016.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = Array(classOf[ProxmockApplication]))
class ProxmockConfiguration {
  @Bean
  def config() = ProxmockConfiguration.config
}

object ProxmockConfiguration {
  var config: Config = null
}

class ProxmockApplication(config: Config, args: Array[String]) {
  ProxmockConfiguration.config = config

  private val udp = new Udp().listen(remoteControlMessageHandler)

  private val configuration: Array[Object] = Array(classOf[ProxmockConfiguration])
  private val context = SpringApplication.run(configuration, SpringArguments(config, args))

  def close() = {
    println("Closing ProxmockApplication")
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


object ProxmockApplication {
  def run(filePath: String, metaArgs: Array[String]) = {
    val configFile = new File(filePath)
    val content = readFileToString(configFile, UTF_8)
    val config = Yaml.parse(content, classOf[Config])
    config.configRootDir = Option(configFile.getParent)

    new ProxmockApplication(config, metaArgs)
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