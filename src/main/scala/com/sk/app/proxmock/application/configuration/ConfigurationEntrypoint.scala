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

  @Bean
  def startSetup(@Autowired config: Config): Object = {
    config.configure(context)
    new Object()
  }
}
