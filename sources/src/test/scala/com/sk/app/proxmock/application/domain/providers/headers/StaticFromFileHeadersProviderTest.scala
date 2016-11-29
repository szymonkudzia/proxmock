package com.sk.app.proxmock.application.domain.providers.headers

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class StaticFromFileHeadersProviderTest extends BaseIntegrationTest("/mock") {
  val file = createTempFile(
    """
      |header_1: value_1
      |header_2: value_2
    """.stripMargin
  )

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("static/from/file/headers/provider")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      headers:
      |        staticFromFile: ${file.getAbsoluteFile}
    """.stripMargin


  "StaticFromFileHeadersProvider" should "return headers loaded from file with no changes (as is)" in {
    get(url("static/from/file/headers/provider"))
        .`then`()
           .header("header_1", equalTo("value_1"))
           .header("header_2", equalTo("value_2"))
  }
}
