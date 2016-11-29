package com.sk.app.proxmock.application.domain.providers.body

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class GroovyExpressionBodyProviderTest extends BaseIntegrationTest("/mock") {
  override def endpointsYaml(): String =
    s"""
      |- path: ${url("groovy/expression/body/provider")}
      |  method: POST
      |  action:
      |    mockResponse:
      |      body:
      |        groovyExpression: message.payload
    """

  "GroovyExpressionBodyProviderTest" should "return request body content (request body defined)" in {
    given()
        .body("expected body content")
      .when()
        .post(url("groovy/expression/body/provider"))
      .`then`()
        .body(equalTo("expected body content"))
  }

  "GroovyExpressionBodyProviderTest" should "return request body content (request body undefined)" in {
    post(url("groovy/expression/body/provider"))
      .`then`()
        .body(equalTo("{}")) // spring treats empty body as empty json
  }
}
