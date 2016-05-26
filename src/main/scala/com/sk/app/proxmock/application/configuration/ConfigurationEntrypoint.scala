package com.sk.app.proxmock.application.configuration

import com.sk.app.proxmock.application.domain.Config
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.{Bean, Configuration}

/**
 * Created by Szymon on 23.05.2016.
 */
@Configuration
class ConfigurationEntrypoint {
  @Autowired
  val context: ConfigurableApplicationContext = null

  @Autowired
  val config: Config = null

  @Bean
  def startSetup(): String = {
    config.configure(context)
    "done"
  }
}
