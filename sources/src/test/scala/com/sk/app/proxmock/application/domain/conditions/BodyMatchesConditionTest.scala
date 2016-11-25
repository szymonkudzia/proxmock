package com.sk.app.proxmock.application.domain.conditions

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class BodyMatchesConditionTest extends BaseIntegrationTest("/mock") {
  val expectedBodyValueIfTrue = "body matches ifTrue body"
  val expectedBodyValueIfFalse = "body matches ifFalse body"

  override def endpointsYaml(): String =
    s"""
       |- path: ${url("body/matches/condition")}
       |  method: POST
       |  action:
       |    mockResponse:
       |      body:
       |        conditional:
       |          condition:
       |            bodyMatches: .*EVAL_TO_TRUE.*
       |          ifTrue:
       |            static: $expectedBodyValueIfTrue
       |          ifFalse:
       |            static: $expectedBodyValueIfFalse
    """

  "BodyMatchesCondition" should "eval to true when body contains string 'EVAL_TO_TRUE'" in {
    given()
        .body("body containing EVAL_TO_TRUE string")
      .when()
        .post(url("body/matches/condition"))
      .`then`()
        .body(equalTo(expectedBodyValueIfTrue))
  }

  "BodyMatchesCondition" should "eval to false when body does not contain string 'EVAL_TO_TRUE'" in {
    given()
        .body("body not containing expected string")
      .when()
        .post(url("body/matches/condition"))
      .`then`()
        .body(equalTo(expectedBodyValueIfFalse))
  }

  "BodyMatchesCondition" should "eval to false when body is missing" in {
    post(url("body/matches/condition"))
      .`then`()
        .body(equalTo(expectedBodyValueIfFalse))
  }
}
