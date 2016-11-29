package com.sk.app.proxmock.application.domain.providers.body

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class GroovyExpressionFromFileBodyProviderTest extends BaseIntegrationTest("/mock") {
  val file = createTempFile("message.payload")

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("groovy/expression/from/file/body/provider")}
      |  method: POST
      |  action:
      |    mockResponse:
      |      body:
      |        groovyExpressionFromFile: ${file.getAbsolutePath}
    """

  "GroovyExpressionFromFileBodyProviderTest" should "return body content given by groovy expresion" +
    "loaded from file" in {
    given()
        .body("expected body content")
      .when()
        .post(url("groovy/expression/from/file/body/provider"))
      .`then`()
        .body(equalTo("expected body content"))
  }
}
