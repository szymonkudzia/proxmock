package com.sk.app.proxmock.application.domain.actions

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 30/11/2016.
  */
class GroovyExpressionActionTest extends BaseIntegrationTest("/groovy/expression/action") {
  val expectedBody = "groovy expression body"
  val expectedHeader = "custom_header"
  val expectedHeaderValue = "custom_value"

  override def endpointsYaml(): String =
    s"""
       |- path: ${url("groovy/expression")}
       |  method: GET
       |  action:
       |    groovyExpression: |
       |      MessageBuilder
       |        .withPayload('$expectedBody')
       |        .setHeader('$expectedHeader', '$expectedHeaderValue')
       |        .build()
       |- path: ${url("null/returned")}
       |  method: GET
       |  action:
       |    groovyExpression: return null
    """


  "GroovyExpressionAction" should "return response provided by groovy expression" in {
    get(url("groovy/expression"))
      .`then`()
        .header(expectedHeader, expectedHeaderValue)
        .body(equalTo(expectedBody))
  }

  "GroovyExpressionAction" should "return error response with expected description" in {
    get(url("null/returned"))
      .`then`()
      .body(containsString("GroovyExpression cannot produce null message object"))
  }
}
