package com.sk.app.proxmock.application.configuration

import java.io.File
import java.nio.charset.StandardCharsets

import com.sk.app.proxmock.toolset.serialization.Yaml
import org.apache.commons.io.FileUtils
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.integration.dsl.{IntegrationFlowBuilder, IntegrationFlowDefinition}
import org.springframework.integration.dsl.channel.MessageChannels
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway
import org.springframework.integration.http.support.DefaultHttpHeaderMapper

import scala.collection.immutable.ListSet

/**
 * Created by Szymon on 26.05.2016.
 */
class ConfigurationContext private (
  val springContext: ConfigurableApplicationContext,
  var flowBuilder: IntegrationFlowDefinition[_],
  val configRootDir: String
) {
  var httpGateway: HttpRequestHandlingMessagingGateway = _
  private var outboundHeadersMapping = ListSet[String](DefaultHttpHeaderMapper.HTTP_RESPONSE_HEADER_NAME_PATTERN)

  def addOutboundHeaderPattern(headerPattern: String): Unit = {
    outboundHeadersMapping += headerPattern

    val mapper = new DefaultHttpHeaderMapper()
    mapper.setInboundHeaderNames(Array("*"))
    mapper.setOutboundHeaderNames(outboundHeadersMapping.toArray)
    mapper.setUserDefinedHeaderPrefix(null)

    httpGateway.setHeaderMapper(mapper)
  }

  def register[T](beanName: String, bean: T): T = {
    val initializedBean = springContext.getAutowireCapableBeanFactory.initializeBean(bean, beanName)
    springContext.getBeanFactory.registerSingleton(beanName, initializedBean)
    initializedBean.asInstanceOf[T]
  }

  def newDirectChannel(name: String) = register(name, MessageChannels.direct().get())

  def toFile(path: String): File = {
    var file = new File(configRootDir, path)
    if (!file.exists()) file = new File(path)
    file
  }

  def fileToString(file: File): String = FileUtils.readFileToString(file, StandardCharsets.UTF_8)

  def fileToString(path: String): String = fileToString(toFile(path))

  def fileToMap(path: String): Map[String, String] =
    Yaml.parse(fileToString(path), classOf[Map[String, String]])
}



object ConfigurationContext {
  def apply(springContext: ConfigurableApplicationContext, configRootDir: String) =
    new ConfigurationContext(springContext, new IntegrationFlowBuilder(), configRootDir)

  def apply(springContext: ConfigurableApplicationContext,
            flowBuilder: IntegrationFlowBuilder,
            configRootDir: String) =
    new ConfigurationContext(springContext, flowBuilder, configRootDir)
}