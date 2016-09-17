package com.sk.app.proxmock.application.domain.actions.mock

import com.jayway.restassured.RestAssured._
import com.sk.app.proxmock.BaseIntegrationTest
import org.hamcrest.Matchers._
import org.springframework.context.annotation.{Bean, Configuration}


@Configuration
class StaticMockResponseTestConfig {
  @Bean
  def enpoint(): String = {
    """
      |- path: /staticMockResponseTest
      |  method: GET
      |  action:
      |    staticMockResponse:
      |      statusCode: 500
      |      headers:
      |        header_1: true
      |      bodyContent: mock body content
    """.stripMargin
  }
}

/**
 * Created by Szymon on 29.05.2016.
 */
class StaticMockResponseTest extends BaseIntegrationTest {
  "StaticMockResponse" should "return defined header in headers property" in {
    invokeStaticMockThen()
        .header("header_1", "true")
    }
  
  it should "return defined body content in bodyContent property" in {
    invokeStaticMockThen()
      .body(equalTo("mock body content"))
  }

  ignore should "return response with status code defined in statusCode property" in {
    invokeStaticMockThen()
      .statusCode(500)
  }
  
  def invokeStaticMockThen() = get("/staticMockResponseTest").then()
}
