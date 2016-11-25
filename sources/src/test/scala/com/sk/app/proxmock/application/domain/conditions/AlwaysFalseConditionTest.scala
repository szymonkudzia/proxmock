package com.sk.app.proxmock.application.domain.conditions

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class AlwaysFalseConditionTest extends BaseIntegrationTest("/mock") {
  val expectedBody = "always false expected body"

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("always/false/condition")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      body:
      |        conditional:
      |          condition:
      |            alwaysFalse: {}
      |          ifTrue:
      |            static: wrong body
      |          ifFalse:
      |            static: $expectedBody
    """

  "AlwaysFalseCondition" should "evaluate to false" in {
    get(url("always/false/condition"))
      .`then`()
          .body(equalTo(expectedBody))
  }
}
