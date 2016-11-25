package com.sk.app.proxmock

import java.io.File
import java.nio.file.Files
import java.util.UUID

import com.sk.app.proxmock.testutils.RestAssuredIntegrationTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FlatSpec}
import org.springframework.beans.factory.config.{BeanFactoryPostProcessor, ConfigurableListableBeanFactory}
import org.springframework.boot.test.{SpringApplicationConfiguration, WebIntegrationTest}
import org.springframework.context.annotation.Configuration

import scala.collection.mutable

/**
 * Created by Szymon on 17.09.2016.
 */
@Configuration
@RunWith(classOf[JUnitRunner])
@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = Array(classOf[ProxmockTestConfiguration]))
abstract class BaseIntegrationTest(baseUri: String) extends FlatSpec
  with RestAssuredIntegrationTest with BeanFactoryPostProcessor with BeforeAndAfterAll {
  val tempFiles = mutable.ArrayBuffer[File]()


  override def afterAll(): Unit = {
    tempFiles.foreach(_.delete())
  }


  /**
    * create bean with endpoint configuration but with random name otherwise
    * each test would override its own endpoints configuration
    */
  override def postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) = {
    beanFactory.registerSingleton(UUID.randomUUID().toString, endpointsYaml().stripMargin)
  }

  def endpointsYaml(): String

  def url(uri: String): String = s"$baseUri/$uri"

  def createTempFile(suffix: String, content: String): File = {
    val file = File.createTempFile(baseUri.replaceAll("/", "_"), suffix)
    tempFiles += file
    Files.write(file.toPath, content.getBytes)

    file
  }

}
