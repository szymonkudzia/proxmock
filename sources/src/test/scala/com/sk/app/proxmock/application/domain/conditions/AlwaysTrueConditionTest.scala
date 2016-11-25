package com.sk.app.proxmock.application.domain.conditions

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class AlwaysTrueConditionTest extends BaseIntegrationTest("/mock") {
  val expectedBody = "always true expected body"

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("always/true/condition")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      body:
      |        conditional:
      |          condition:
      |            alwaysTrue: {}
      |          ifTrue:
      |            static: $expectedBody
      |          ifFalse:
      |            static: wrong body
    """

  "AlwaysTrueCondition" should "evaluate to true" in {
    get(url("always/true/condition"))
      .`then`()
          .body(equalTo(expectedBody))
  }
}
