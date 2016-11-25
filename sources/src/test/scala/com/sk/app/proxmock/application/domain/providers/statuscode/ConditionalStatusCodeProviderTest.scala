package com.sk.app.proxmock.application.domain.providers.statuscode

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest

/**
  * Created by szymo on 25/11/2016.
  */
class ConditionalStatusCodeProviderTest extends BaseIntegrationTest("/mock/conditional/status/code/provider") {
  val expectedStatusCodeIfTrue = 404
  val expectedStatusCodeIfFalse = 505

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("condition/is/true")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      statusCode:
      |        conditional:
      |          condition:
      |            alwaysTrue: {}
      |          ifTrue:
      |            static: $expectedStatusCodeIfTrue
      |          ifFalse:
      |            static: $expectedStatusCodeIfFalse
      |- path: ${url("condition/is/false")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      statusCode:
      |        conditional:
      |          condition:
      |            alwaysFalse: {}
      |          ifTrue:
      |            static: $expectedStatusCodeIfTrue
      |          ifFalse:
      |            static: $expectedStatusCodeIfFalse
    """


  "ConditionalStatusCodeProvider" should "return value given by **ifTrue** provider when condition is true" in {
    get(url("condition/is/true"))
      .`then`()
        .statusCode(expectedStatusCodeIfTrue)
  }

  "ConditionalStatusCodeProvider" should "return value given by **ifFalse** provider when condition is false" in {
    get(url("condition/is/false"))
      .`then`()
        .statusCode(expectedStatusCodeIfFalse)
  }
}
