package com.sk.app.proxmock.application.domain

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.context.ConfigurableApplicationContext

/**
 * Created by Szymon on 18.05.2016.
 */
case class Config(
   name: String,
   port: String = "8080",
   endpoints: List[Endpoint]
) {
   var configRootDir: Option[String] = None

   def configure(springContext: ConfigurableApplicationContext): Unit = {
      endpoints.foreach(_.configure(ConfigurationContext(springContext, configRootDir.getOrElse(""))))
   }
}

