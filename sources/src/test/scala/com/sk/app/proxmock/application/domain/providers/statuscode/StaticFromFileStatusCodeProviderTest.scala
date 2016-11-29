package com.sk.app.proxmock.application.domain.providers.statuscode

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest

/**
  * Created by szymo on 25/11/2016.
  */
class StaticFromFileStatusCodeProviderTest extends BaseIntegrationTest("/mock") {
  val file = createTempFile("300")

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("static/from/file/status/code/provider")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      statusCode:
      |        staticFromFile: ${file.getAbsolutePath}
    """

  "StaticFromFileStatusCodeProvider" should "return status code loaded from file" in {
    get(url("static/from/file/status/code/provider"))
      .`then`()
        .statusCode(300)
  }
}
