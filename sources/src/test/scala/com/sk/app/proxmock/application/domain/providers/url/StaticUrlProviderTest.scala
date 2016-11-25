package com.sk.app.proxmock.application.domain.providers.url

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class StaticUrlProviderTest extends BaseIntegrationTest("/proxy/static/url/provider") {
  val expectedBody = "expectedBody"

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("static/url")}
      |  action:
      |    proxy:
      |      toUrl:
      |        static: http://localhost${url("expected/address")}
      |- path: ${url("expected/address")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      body:
      |        static: $expectedBody
    """

  "StaticUrlProvider" should "return specified static url without changing it" in {
    get(url("static/url"))
      .`then`()
        .body(equalTo(expectedBody))
  }
}
