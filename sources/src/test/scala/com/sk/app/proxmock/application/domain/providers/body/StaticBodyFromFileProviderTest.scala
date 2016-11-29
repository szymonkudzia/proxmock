package com.sk.app.proxmock.application.domain.providers.body

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class StaticBodyFromFileProviderTest extends BaseIntegrationTest("/mock") {
  val expectedBody = "static body file expected body content"
  val file = createTempFile(expectedBody)

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("static/body/file/provider")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      body:
      |        staticFromFile: ${file.getAbsolutePath}
    """

  "StaticBodyFileProvider" should "return specified body content loaded " +
    "from file with no changes (as is)" in {
    get(url("static/body/file/provider"))
      .`then`()
          .body(equalTo(expectedBody))
  }
}
