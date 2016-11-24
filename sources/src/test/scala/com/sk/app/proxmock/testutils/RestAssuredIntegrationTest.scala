package com.sk.app.proxmock.testutils

import com.jayway.restassured.RestAssured
import org.scalatest.Suite
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.core.env.Environment

/**
 * Created by Szymon on 21.11.2015.
 *
 * Manages RestAssured by configuring port on which spring application
 * listens for incoming requests
 */

trait RestAssuredIntegrationTest extends SpringContext { this: Suite =>
  @Autowired
  val environment: Environment = null

  abstract override def beforeAll(): Unit = {
    super.beforeAll()
    RestAssured.port = environment.getProperty("local.server.port", classOf[Integer])
  }
}
