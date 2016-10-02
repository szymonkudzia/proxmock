package com.sk.app.proxmock.application.domain.actions.mock

import java.nio.file.StandardOpenOption._
import java.nio.file.{Files, Paths}

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._
import org.scalatest.BeforeAndAfterEach
import org.springframework.context.annotation.{Bean, Configuration}

object TestData {
  val definitionPath = Paths.get("staticMockResponse_definitionFile.yaml")
  val statusCodePath = Paths.get("staticMockResponse_statusCodeFile.yaml")
  val headersPath = Paths.get("staticMockResponse_headersFile.yaml")
  val bodyPath = Paths.get("staticMockResponse_bodyFile.yaml")
}

@Configuration
class StaticMockResponseTestConfig {
  @Bean
  def enpoint(): String = {
    s"""
      |- path: /staticMockResponseTest
      |  method: GET
      |  action:
      |    staticMockResponse:
      |      statusCode: 500
      |      headers:
      |        header_1: true
      |      bodyContent: mock body content
      |- path: /staticMockResponseTest_statusCodePath
      |  method: GET
      |  action:
      |    staticMockResponse:
      |      statusCodePath: ${TestData.statusCodePath}
      |      bodyContent: ok
      |- path: /staticMockResponseTest_headersPath
      |  method: GET
      |  action:
      |    staticMockResponse:
      |      headers:
      |        header_1: first header
      |        header_2: header which should be overridden by file header
      |      headersPath: ${TestData.headersPath}
      |      bodyContent: ok
      |- path: /staticMockResponseTest_bodyPath
      |  method: GET
      |  action:
      |    staticMockResponse:
      |      bodyPath: ${TestData.bodyPath}
    """.stripMargin
  }
}

/**
 * Created by Szymon on 29.05.2016.
 */
class StaticMockResponseTest extends BaseIntegrationTest with BeforeAndAfterEach {



  "StaticMockResponse" should "return defined header in headers property" in {
    get("/staticMockResponseTest")
      .then()
        .header("header_1", "true")
    }
  
  it should "return defined body content in bodyContent property" in {
    get("/staticMockResponseTest")
      .then()
        .body(equalTo("mock body content"))
  }

  it should "return response with status code defined in statusCode property" in {
    get("/staticMockResponseTest")
      .then()
        .statusCode(500)
  }

  it should "return response with status code defined in file under statusCodePath" in {
    get("/staticMockResponseTest_statusCodePath")
      .then()
        .statusCode(400)
  }

  it should "return response with headers defined in file under headersPath" in {
    get("/staticMockResponseTest_headersPath")
      .then()
        .header("header_1", "first header")
        .header("header_2", "overriding value from file")
        .header("header_3", "header from file")
  }

  it should "return defined body content in file under bodyPath" in {
    get("/staticMockResponseTest_bodyPath")
      .then()
        .body(equalTo("body content from file"))
  }

  override protected def beforeEach() = {
    super.beforeEach()
    Files.write(TestData.statusCodePath, "400".getBytes, CREATE)
    Files.write(TestData.bodyPath, "body content from file".getBytes, CREATE)
    Files.write(TestData.headersPath,
      ("header_2: overriding value from file\n" +
       "header_3: header from file").getBytes, CREATE)

    Files.write(TestData.definitionPath,
      s"""
         |statusCode: 300
         |headers:
         |  header_1: header from definition file
         |bodyContent: body from definition file
         """.stripMargin.getBytes,
      CREATE
    )
  }

  override protected def afterEach() = {
    Files.delete(TestData.statusCodePath)
    Files.delete(TestData.headersPath)
    Files.delete(TestData.bodyPath)
    Files.delete(TestData.definitionPath)

    super.afterEach()
  }

}
