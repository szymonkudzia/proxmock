package com.sk.app.proxmock

import java.util.UUID

import com.sk.app.proxmock.testutils.RestAssuredIntegrationTest
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import org.springframework.beans.factory.config.{BeanFactoryPostProcessor, ConfigurableListableBeanFactory}
import org.springframework.boot.test.{SpringApplicationConfiguration, WebIntegrationTest}
import org.springframework.context.annotation.Configuration

/**
 * Created by Szymon on 17.09.2016.
 */
@Configuration
@RunWith(classOf[JUnitRunner])
@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = Array(classOf[ProxmockTestConfiguration]))
abstract class BaseIntegrationTest(baseUrl: String) extends FlatSpec with RestAssuredIntegrationTest with BeanFactoryPostProcessor {
  /**
    * create bean with endpoint configuration but with random name otherwise
    * each test would override its own endpoints configuration
    */
  override def postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) = {
    beanFactory.registerSingleton(UUID.randomUUID().toString, endpointsYaml().stripMargin)
  }

  def endpointsYaml(): String

  def url(uri: String): String = s"$baseUrl/$uri"
}
