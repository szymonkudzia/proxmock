package com.sk.app.proxmock.application.domain.providers.url

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._

/**
  * Created by szymo on 25/11/2016.
  */
class GroovyExpressionFromFileUrlProviderTest extends BaseIntegrationTest("/proxy/groovy/expression/from/file/url/provider") {
  val expectedBody = "expectedBody"
  val file = createTempFile(s"return 'http://localhost${url("expected/address")}'")

  override def endpointsYaml(): String =
    s"""
      |- path: ${url("correct/url")}
      |  action:
      |    proxy:
      |      toUrl:
      |        groovyExpressionFromFile: ${file.getAbsolutePath}
      |- path: ${url("expected/address")}
      |  method: GET
      |  action:
      |    mockResponse:
      |      body:
      |        static: $expectedBody
    """

  "GroovyExpressionFromFileUrlProvider" should "return url returned by groovy expression loaded from file" in {
    get(url("correct/url"))
      .`then`()
        .body(equalTo(expectedBody))
  }
}
