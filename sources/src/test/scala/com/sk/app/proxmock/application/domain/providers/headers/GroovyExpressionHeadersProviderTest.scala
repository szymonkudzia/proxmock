package com.sk.app.proxmock.application.domain.providers.headers

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class GroovyExpressionHeadersProviderTest extends BaseIntegrationTest("/mock") {

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("groovy/expression/headers/provider")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      headers:
      |        groovyExpression: "[header_1: 'value_1', header_2: 'value_2']"
    """.stripMargin


  "GroovyExpressionHeadersProvider" should "return headers created by groovy expression" in {
    get(url("groovy/expression/headers/provider"))
        .`then`()
           .header("header_1", equalTo("value_1"))
           .header("header_2", equalTo("value_2"))
  }
}
