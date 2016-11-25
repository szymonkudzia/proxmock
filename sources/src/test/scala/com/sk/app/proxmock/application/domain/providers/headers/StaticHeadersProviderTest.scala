package com.sk.app.proxmock.application.domain.providers.headers

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class StaticHeadersProviderTest extends BaseIntegrationTest("/mock") {
  override def endpointsYaml(): String =
    s"""
      |- path: ${url("static/headers/provider")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      headers:
      |        static:
      |          testHeader: testValue
    """.stripMargin


  "StaticHeadersProvider" should "return specified headers with no changes (as is)" in {
    get(url("static/headers/provider"))
        .`then`()
           .header("testHeader", equalTo("testValue"))
  }
}
