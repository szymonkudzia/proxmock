package com.sk.app.proxmock.application.domain.conditions

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class GroovyExpressionConditionTest extends BaseIntegrationTest("/mock/groovy/expression/condition") {
  val expectedBodyValueIfTrue = "groovy expression ifTrue body"
  val expectedBodyValueIfFalse = "groovy expression ifFalse body"

  override def endpointsYaml(): String =
    s"""
       |- path: ${url("header/present")}
       |  method: POST
       |  action:
       |    mockResponse:
       |      body:
       |        conditional:
       |          condition:
       |            groovyExpression: message.headers.containsKey('expected_header')
       |          ifTrue:
       |            static: $expectedBodyValueIfTrue
       |          ifFalse:
       |            static: $expectedBodyValueIfFalse
       |- path: ${url("expression/returns/object")}
       |  method: POST
       |  action:
       |    mockResponse:
       |      body:
       |        conditional:
       |          condition:
       |            groovyExpression: "message.headers.containsKey('expected_header') ? message : null"
       |          ifTrue:
       |            static: $expectedBodyValueIfTrue
       |          ifFalse:
       |            static: $expectedBodyValueIfFalse
       |- path: ${url("expression/returns/string")}
       |  method: POST
       |  action:
       |    mockResponse:
       |      body:
       |        conditional:
       |          condition:
       |            groovyExpression: "message.headers.containsKey('expected_header') ? 'true' : 'false'"
       |          ifTrue:
       |            static: $expectedBodyValueIfTrue
       |          ifFalse:
       |            static: $expectedBodyValueIfFalse
    """


  "GroovyExpressionConditionTest" should "eval to true when header 'expected_header' is present" in {
    given()
        .header("expected_header", "present")
      .when()
        .post(url("header/present"))
      .`then`()
        .body(equalTo(expectedBodyValueIfTrue))
  }

  "GroovyExpressionConditionTest" should "eval to false when header 'expected_header' is not present" in {
    post(url("header/present"))
      .`then`()
        .body(equalTo(expectedBodyValueIfFalse))
  }



  "GroovyExpressionConditionTest" should "eval to true when groovy expression returns not null object" in {
    given()
        .header("expected_header", "present")
      .when()
        .post(url("expression/returns/object"))
      .`then`()
        .body(equalTo(expectedBodyValueIfTrue))
  }

  "GroovyExpressionConditionTest" should "eval to false when groovy expression returns null" in {
    post(url("expression/returns/object"))
      .`then`()
        .body(equalTo(expectedBodyValueIfFalse))
  }



  "GroovyExpressionConditionTest" should "eval to true when groovy expression returns string 'true'" in {
    given()
      .header("expected_header", "present")
      .when()
      .post(url("expression/returns/string"))
      .`then`()
      .body(equalTo(expectedBodyValueIfTrue))
  }

  "GroovyExpressionConditionTest" should "eval to false when groovy expression returns string 'false'" in {
    post(url("expression/returns/string"))
      .`then`()
      .body(equalTo(expectedBodyValueIfFalse))
  }
}
