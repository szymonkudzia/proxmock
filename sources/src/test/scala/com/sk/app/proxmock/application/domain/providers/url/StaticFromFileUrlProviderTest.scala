package com.sk.app.proxmock.application.domain.providers.url

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class StaticFromFileUrlProviderTest extends BaseIntegrationTest("/proxy/static/from/file/url/provider") {
  val expectedBody = "expectedBody"
  val file = createTempFile(s"http://localhost${url("expected/address")}")

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("static/from/file/url")}
      |  action:
      |    proxy:
      |      toUrl:
      |        staticFromFile: ${file.getAbsolutePath}
      |- path: ${url("expected/address")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      body:
      |        static: $expectedBody
    """

  "StaticFromFileUrlProvider" should "return url loaded from file" in {
    get(url("static/from/file/url"))
      .`then`()
        .body(equalTo(expectedBody))
  }
}
