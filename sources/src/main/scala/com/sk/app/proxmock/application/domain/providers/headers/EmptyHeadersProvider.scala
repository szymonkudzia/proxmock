package com.sk.app.proxmock.application.domain.providers.headers

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
 * Created by Szymon on 06.10.2016.
 */
case class EmptyHeadersProvider() extends HeadersProvider {
  override def get(context: ConfigurationContext, message: Message[Object]): Map[String, String] =
    Map()
}
