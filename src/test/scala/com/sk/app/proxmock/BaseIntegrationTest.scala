package com.sk.app.proxmock

import com.sk.app.proxmock.testutils.RestAssuredIntegrationTest
import org.scalatest.FlatSpec
import org.springframework.boot.test.{SpringApplicationConfiguration, WebIntegrationTest}

/**
 * Created by Szymon on 17.09.2016.
 */
@WebIntegrationTest(Array("server.port:0"))
@SpringApplicationConfiguration(classes = Array(classOf[ProxmockTestConfiguration]))
class BaseIntegrationTest extends FlatSpec with RestAssuredIntegrationTest {

}
