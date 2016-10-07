package com.sk.app.proxmock.application.domain.providers.statuscode

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
 * Created by Szymon on 06.10.2016.
 */
case class SuccessStatusCodeProvider() extends StatusCodeProvider {
  override def get(context: ConfigurationContext, message: Message[Object]): Int = 200
}
