package com.sk.app.proxmock.application.domain.providers.statuscode

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest

/**
  * Created by szymo on 25/11/2016.
  */
class GroovyExpressionFromFileStatusCodeProviderTest extends BaseIntegrationTest("/mock") {
  val file = createTempFile("10 + 500 - 210")

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("groovy/expression/from/file/status/code/provider")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      statusCode:
      |        groovyExpressionFromFile: ${file.getAbsolutePath}
    """

  "GroovyExpressionFromFileStatusCodeProvider" should "return status code returned by groovy expression" +
    "loaded from file" in {
    get(url("groovy/expression/from/file/status/code/provider"))
      .`then`()
        .statusCode(300)
  }
}
