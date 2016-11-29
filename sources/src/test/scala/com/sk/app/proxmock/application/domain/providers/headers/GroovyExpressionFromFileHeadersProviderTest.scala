package com.sk.app.proxmock.application.domain.providers.headers

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class GroovyExpressionFromFileHeadersProviderTest extends BaseIntegrationTest("/mock") {
  val file = createTempFile("[header_1: 'value_1', header_2: 'value_2']")

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("groovy/expression/from/file/headers/provider")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      headers:
      |        groovyExpressionFromFile: ${file.getAbsolutePath}
    """.stripMargin


  "GroovyExpressionFromFileHeadersProvider" should "return headers created by groovy expression" +
    "loaded from file" in {
    get(url("groovy/expression/from/file/headers/provider"))
        .`then`()
           .header("header_1", equalTo("value_1"))
           .header("header_2", equalTo("value_2"))
  }
}
