package com.sk.app.proxmock.application.domain.providers.body

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers

/**
  * Created by szymo on 24/11/2016.
  */
class ConditionalBodyProviderTest extends BaseIntegrationTest("/mock/conditional/body/provider") {
  val responseWhenConditionIsTrue = "responseWhenConditionIsTrue"
  val responseWhenConditionIsFalse = "responseWhenConditionIsFalse"

  override def endpointsYaml(): String = s"""
      |- path: ${url("condition/is/true")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      body:
      |        conditional:
      |          condition:
      |            alwaysTrue: {}
      |          ifTrue:
      |            static: $responseWhenConditionIsTrue
      |          ifFalse:
      |            static: $responseWhenConditionIsFalse
      |- path: ${url("condition/is/false")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      body:
      |        conditional:
      |          condition:
      |            alwaysFalse: {}
      |          ifTrue:
      |            static: $responseWhenConditionIsTrue
      |          ifFalse:
      |            static: $responseWhenConditionIsFalse
    """

  "MockResponse" should
    "return body given by **ifTrue** provider when condition in ConditionalBodyProvider is true" in {
    get(url("condition/is/true"))
      .`then`()
        .body(Matchers.equalTo(responseWhenConditionIsTrue))
  }

  "MockResponse" should
    "return body given by **ifFalse** provider when condition in ConditionalBodyProvider is false" in {
    get(url("condition/is/false"))
      .`then`()
      .body(Matchers.equalTo(responseWhenConditionIsFalse))
  }
}
