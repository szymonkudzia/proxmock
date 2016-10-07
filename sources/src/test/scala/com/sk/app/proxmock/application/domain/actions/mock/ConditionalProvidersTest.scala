package com.sk.app.proxmock.application.domain.actions.mock

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import com.sk.app.proxmock.application.domain.actions.mock.TestConfig._
import org.hamcrest.Matchers._
import org.scalatest.BeforeAndAfterEach
import org.springframework.context.annotation._


@Configuration
class TestConfig {
  @Bean
  def enpoint(): String = {
    s"""
      |- path: ${url("ifTrue_path")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      statusCode:
      |        conditional:
      |          condition:
      |            alwaysTrue: {}
      |          ifTrue:
      |            static: $ifTrueStatusCode
      |      headers:
      |        conditional:
      |          condition:
      |            alwaysTrue: {}
      |          ifTrue:
      |            static:
      |              $header: $ifTrueHeader
      |      body:
      |        conditional:
      |          condition:
      |            alwaysTrue: {}
      |          ifTrue:
      |            static: $ifTrueBody
      |- path: ${url("ifFalse_path")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      statusCode:
      |        conditional:
      |          condition:
      |            alwaysFalse: {}
      |          ifTrue:
      |            static: $ifTrueStatusCode
      |          ifFalse:
      |            static: $ifFalseStatusCode
      |      headers:
      |        conditional:
      |          condition:
      |            alwaysFalse: {}
      |          ifTrue:
      |            static:
      |              $header: $ifTrueHeader
      |          ifFalse:
      |            static:
      |              $header: $ifFalseHeader
      |      body:
      |        conditional:
      |          condition:
      |            alwaysFalse: {}
      |          ifTrue:
      |            static: $ifTrueBody
      |          ifFalse:
      |            static: $ifFalseBody
    """.stripMargin
  }
}

object TestConfig {
  def url(testCase: String) = s"/mock/conditionalStatusCode_$testCase"

  val ifTrueStatusCode = 505
  val ifFalseStatusCode = 405
  
  val header = "testHeader"
  val ifTrueHeader = "true"
  val ifFalseHeader = "false"
  
  val ifTrueBody = "true"
  val ifFalseBody = "false"
}

/**
 * Created by Szymon on 29.05.2016.
 */
class ConditionalProvidersTest extends BaseIntegrationTest with BeforeAndAfterEach {
  
  "ConditionalStatusCode" should "use ifTrue provider when condition evals to true" in {
    get(url("ifTrue_path"))
      .then()
        .statusCode(ifTrueStatusCode)
    }

  "ConditionalStatusCode" should "use ifFalse provider when condition evals to false" in {
    get(url("ifFalse_path"))
      .then()
        .statusCode(ifFalseStatusCode)
  }




  "ConditionalHeaders" should "use ifTrue provider when condition evals to true" in {
    get(url("ifTrue_path"))
      .then()
        .header(header, ifTrueHeader)
  }

  "ConditionalHeaders" should "use ifFalse provider when condition evals to false" in {
    get(url("ifFalse_path"))
      .then()
        .header(header, ifFalseHeader)
  }



  "ConditionalBodyProvider" should "use ifTrue provider when condition evals to true" in {
    get(url("ifTrue_path"))
      .then()
        .body(equalTo(ifTrueBody))
  }

  "ConditionalBodyProvider" should "use ifFalse provider when condition evals to false" in {
    get(url("ifFalse_path"))
      .then()
        .body(equalTo(ifFalseBody))
  }
}
