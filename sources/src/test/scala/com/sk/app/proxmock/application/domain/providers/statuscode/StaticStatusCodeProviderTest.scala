package com.sk.app.proxmock.application.domain.providers.statuscode

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest

/**
  * Created by szymo on 25/11/2016.
  */
class StaticStatusCodeProviderTest extends BaseIntegrationTest("/mock") {
  override def endpointsYaml(): String =
    s"""
      |- path: ${url("static/status/code/provider")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      statusCode:
      |        static: 300
    """

  "StaticStatusCodeProvider" should "return specified value with no changes (as is)" in {
    get(url("static/status/code/provider"))
      .`then`()
        .statusCode(300)
  }
}
