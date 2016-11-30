package com.sk.app.proxmock.application.domain.actions

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 30/11/2016.
  */
class FirstMetConditionActionTest extends BaseIntegrationTest("/first/met/condition") {
  override def endpointsYaml(): String =
    s"""
       |- path: ${url("")}
       |  method: GET
       |  action:
       |    firstMetCondition:
       |    - condition:
       |        uriMatches: .*FIRST_CASE.*
       |      action:
       |        mockResponse:
       |          body:
       |            static: FIRST_CASE
       |    - condition:
       |        uriMatches: .*SECOND_CASE.*
       |      action:
       |        mockResponse:
       |          body:
       |            static: SECOND_CASE
       |    - condition:
       |        alwaysTrue: {}
       |      action:
       |        mockResponse:
       |          body:
       |            static: DEFAULT
    """


  "FirstMetCondition" should "return response provided by first action when first condition is met" in {
    get(url("?a=FIRST_CASE"))
      .`then`()
        .body(equalTo("FIRST_CASE"))
  }

  "FirstMetCondition" should "return response provided by second action when second condition is met" in {
    get(url("?a=SECOND_CASE"))
      .`then`()
      .body(equalTo("SECOND_CASE"))
  }

  "FirstMetCondition" should "return response provided by last action when no previous conditions were met" +
    "(last condition is always true)" in {
    get(url("?a=DEFAULT"))
      .`then`()
      .body(equalTo("DEFAULT"))
  }
}
