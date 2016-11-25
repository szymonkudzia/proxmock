package com.sk.app.proxmock.application.domain.providers.headers

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class ConditionalHeadersProviderTest extends BaseIntegrationTest("/mock/conditional/headers/provider") {
  val expectedValueIfTrue = "expectedValueIfTrue"
  val expectedValueIfFalse = "expectedValueIfFalse"

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("condition/is/true")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      headers:
      |        conditional:
      |          condition:
      |            alwaysTrue: {}
      |          ifTrue:
      |            static:
      |              testHeader: $expectedValueIfTrue
      |          ifFalse:
      |            static:
      |              testHeader: $expectedValueIfFalse
      |- path: ${url("condition/is/false")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      headers:
      |        conditional:
      |          condition:
      |            alwaysFalse: {}
      |          ifTrue:
      |            static:
      |              testHeader: $expectedValueIfTrue
      |          ifFalse:
      |            static:
      |              testHeader: $expectedValueIfFalse
    """.stripMargin


  "ConditionalHeadersProvider" should "return value given by **ifTrue** provider when condition is true" in {
    get(url("condition/is/true"))
      .`then`()
        .header("testHeader", equalTo(expectedValueIfTrue))
  }

  "ConditionalHeadersProvider" should "return value given by **ifFalse** provider when condition is false" in {
    get(url("condition/is/false"))
      .`then`()
        .header("testHeader", equalTo(expectedValueIfFalse))
  }
}
