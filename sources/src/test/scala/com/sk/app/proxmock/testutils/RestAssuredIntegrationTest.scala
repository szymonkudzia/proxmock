package com.sk.app.proxmock.testutils

import com.jayway.restassured.RestAssured
import org.scalatest.Suite
import org.springframework.beans.factory.annotation.Value

/**
 * Created by Szymon on 21.11.2015.
 *
 * Manages RestAssured by configuring port on which spring application
 * listens for incoming requests
 */

trait RestAssuredIntegrationTest extends SpringContext { this: Suite =>
  @Value("${local.server.port}")
  val serverPort: Integer = null

  abstract override def beforeAll(): Unit = {
    super.beforeAll()
    RestAssured.port = serverPort
  }
}
