package com.sk.app.proxmock.application.domain.actions

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 30/11/2016.
  */
class GroovyExpressionFromFileActionTest extends BaseIntegrationTest("/groovy/expression/from/file/action") {
  val expectedBody = "groovy expression body"
  val expectedHeader = "custom_header"
  val expectedHeaderValue = "custom_value"

  val file = createTempFile(
    s"""
      |MessageBuilder
      |        .withPayload('$expectedBody')
      |        .setHeader('$expectedHeader', '$expectedHeaderValue')
      |        .build()
    """.stripMargin)

  override def endpointsYaml(): String =
    s"""
       |- path: ${url("groovy/expression/from/file")}
       |  method: GET
       |  action:
       |    groovyExpressionFromFile: ${file.getAbsolutePath}
    """


  "GroovyExpressionFromFileAction" should "return response provided by groovy expression loaded from file" in {
    get(url("groovy/expression/from/file"))
      .`then`()
        .header(expectedHeader, expectedHeaderValue)
        .body(equalTo(expectedBody))
  }
}
