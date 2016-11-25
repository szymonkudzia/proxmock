package com.sk.app.proxmock.application.domain.providers.body

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class StaticBodyProviderTest extends BaseIntegrationTest("/mock") {
  val expectedBody = "expectedBodyValue"

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("static/body/provider")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      body:
      |        static: "$expectedBody"
    """

  "StaticBodyProvider" should "return specified body with no changes (as is)" in {
    get(url("static/body/provider"))
      .`then`()
          .body(equalTo(expectedBody))
  }
}
