package com.sk.app.proxmock.application.domain.providers.statuscode

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest

/**
  * Created by szymo on 25/11/2016.
  */
class SuccessStatusCodeProviderTest extends BaseIntegrationTest("/mock") {
  override def endpointsYaml(): String =
    s"""
      |- path: ${url("success/status/code/provider")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      statusCode:
      |        success: {}
    """

  "SuccessStatusCodeProvider" should "return 200 as status code" in {
    get(url("success/status/code/provider"))
      .`then`()
        .statusCode(200)
  }
}
