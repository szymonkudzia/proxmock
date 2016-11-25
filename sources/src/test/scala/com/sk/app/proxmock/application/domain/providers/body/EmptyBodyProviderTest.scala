package com.sk.app.proxmock.application.domain.providers.body

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers

/**
  * Created by szymo on 24/11/2016.
  */
class EmptyBodyProviderTest extends BaseIntegrationTest("/mock") {
  override def endpointsYaml(): String = s"""
      |- path: ${url("empty/body/provider")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      body:
      |        empty: {}
    """

  "EmptyBodyProvider" should "return empty body" in {
    get(url("empty/body/provider"))
      .`then`()
          .body(Matchers.isEmptyString)
  }
}
