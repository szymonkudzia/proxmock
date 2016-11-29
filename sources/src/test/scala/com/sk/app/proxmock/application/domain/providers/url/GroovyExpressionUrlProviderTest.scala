package com.sk.app.proxmock.application.domain.providers.url

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class GroovyExpressionUrlProviderTest extends BaseIntegrationTest("/proxy/groovy/expression/url/provider") {
  val expectedBody = "expectedBody"

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("correct/url")}
      |  action:
      |    proxy:
      |      toUrl:
      |        groovyExpression: "return 'http://localhost${url("expected/address")}'"
      |- path: ${url("null/url")}
      |  action:
      |    proxy:
      |      toUrl:
      |        groovyExpression: "return null"
      |- path: ${url("expected/address")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      body:
      |        static: $expectedBody
    """

  "GroovyExpressionUrlProvider" should "return url returned by groovy expression" in {
    get(url("correct/url"))
      .`then`()
        .body(equalTo(expectedBody))
  }

  "GroovyExpressionUrlProvider" should "return error containing info that returned url cannot be null" in {
    get(url("null/url"))
      .`then`()
        .statusCode(500)
        .body(containsString("groovyExpression cannot return nullable url"))
  }
}
