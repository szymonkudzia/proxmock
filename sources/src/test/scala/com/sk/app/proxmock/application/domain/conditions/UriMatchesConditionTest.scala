package com.sk.app.proxmock.application.domain.conditions

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class UriMatchesConditionTest extends BaseIntegrationTest("/mock") {
  val expectedBodyValueIfTrue = "uri matches ifTrue body"
  val expectedBodyValueIfFalse = "uri matches ifFalse body"

  override def endpointsYaml(): String =
    s"""
       |- path: ${url("uri/matches/condition")}
       |  method: POST
       |  action:
       |    mockResponse:
       |      body:
       |        conditional:
       |          condition:
       |            uriMatches: .*?condition=TRUE.*
       |          ifTrue:
       |            static: $expectedBodyValueIfTrue
       |          ifFalse:
       |            static: $expectedBodyValueIfFalse
    """

  "UriMatchesConditionTest" should "eval to true when request uri contains string '?condition=TRUE'" in {
    post(url("uri/matches/condition?condition=TRUE&someVar=1"))
      .`then`()
        .body(equalTo(expectedBodyValueIfTrue))
  }

  "HeaderMatchesConditionTest" should "eval to false when request uri does not contain string '?condition=TRUE'" in {
    post(url("uri/matches/condition"))
      .`then`()
        .body(equalTo(expectedBodyValueIfFalse))
  }
}
