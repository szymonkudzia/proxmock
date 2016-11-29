package com.sk.app.proxmock.application.domain.conditions

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class GroovyExpressionFileConditionTest extends BaseIntegrationTest("/mock/groovy/expression/file/condition") {
  val expectedBodyValueIfTrue = "groovy expression ifTrue body"
  val expectedBodyValueIfFalse = "groovy expression ifFalse body"

  val groovyExpressionFile = createTempFile("message.headers.containsKey('expected_header')")

  override def endpointsYaml(): String =
    s"""
       |- path: ${url("expression/from/file")}
       |  method: POST
       |  action:
       |    mockResponse:
       |      body:
       |        conditional:
       |          condition:
       |            groovyExpressionFile: ${groovyExpressionFile.getAbsolutePath}
       |          ifTrue:
       |            static: $expectedBodyValueIfTrue
       |          ifFalse:
       |            static: $expectedBodyValueIfFalse
    """


  "GroovyExpressionFileConditionTest" should "eval to true when groovy expression from file evals to true" in {
    given()
        .header("expected_header", "present")
      .when()
        .post(url("expression/from/file"))
      .`then`()
        .body(equalTo(expectedBodyValueIfTrue))
  }

  "GroovyExpressionFileConditionTest" should "eval to false when groovy expression from file evals to false" in {
    post(url("expression/from/file"))
      .`then`()
        .body(equalTo(expectedBodyValueIfFalse))
  }
}
