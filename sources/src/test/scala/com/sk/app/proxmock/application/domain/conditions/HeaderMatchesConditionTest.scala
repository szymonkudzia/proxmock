package com.sk.app.proxmock.application.domain.conditions

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class HeaderMatchesConditionTest extends BaseIntegrationTest("/mock") {
  val expectedBodyValueIfTrue = "header matches ifTrue body"
  val expectedBodyValueIfFalse = "header matches ifFalse body"

  override def endpointsYaml(): String =
    s"""
       |- path: ${url("header/matches/condition")}
       |  method: POST
       |  action:
       |    mockResponse:
       |      body:
       |        conditional:
       |          condition:
       |            headerMatches:
       |              header: CONDITION
       |              pattern: .*EVAL_TO_TRUE.*
       |          ifTrue:
       |            static: $expectedBodyValueIfTrue
       |          ifFalse:
       |            static: $expectedBodyValueIfFalse
    """

  "HeaderMatchesConditionTest" should "eval to true when header CONDITION contains string 'EVAL_TO_TRUE'" in {
    given()
        .header("CONDITION", "body containing EVAL_TO_TRUE string")
      .when()
        .post(url("header/matches/condition"))
      .`then`()
        .body(equalTo(expectedBodyValueIfTrue))
  }

  "HeaderMatchesConditionTest" should "eval to false when header CONDITION does not contain string 'EVAL_TO_TRUE'" in {
    given()
        .header("CONDITION", "body not containing expected string")
      .when()
        .post(url("header/matches/condition"))
      .`then`()
        .body(equalTo(expectedBodyValueIfFalse))
  }

  "HeaderMatchesConditionTest" should "eval to false when header CONDITION is missing" in {
    post(url("header/matches/condition"))
      .`then`()
        .body(equalTo(expectedBodyValueIfFalse))
  }
}
