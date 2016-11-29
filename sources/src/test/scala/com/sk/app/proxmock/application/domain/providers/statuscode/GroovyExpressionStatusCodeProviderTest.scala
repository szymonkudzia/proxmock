package com.sk.app.proxmock.application.domain.providers.statuscode

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest

/**
  * Created by szymo on 25/11/2016.
  */
class GroovyExpressionStatusCodeProviderTest extends BaseIntegrationTest("/mock") {
  override def endpointsYaml(): String =
    s"""
      |- path: ${url("groovy/expression/status/code/provider")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      statusCode:
      |        groovyExpression: 10 + 500 - 210
    """

  "GroovyExpressionStatusCodeProvider" should "return status code returned by groovy expression" in {
    get(url("groovy/expression/status/code/provider"))
      .`then`()
        .statusCode(300)
  }
}
