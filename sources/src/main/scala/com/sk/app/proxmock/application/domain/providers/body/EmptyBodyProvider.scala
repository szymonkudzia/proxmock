package com.sk.app.proxmock.application.domain.providers.body

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
 * Created by Szymon on 06.10.2016.
 */
case class EmptyBodyProvider() extends BodyProvider {
  override def get(context: ConfigurationContext, message: Message[Object]): String = ""
}
