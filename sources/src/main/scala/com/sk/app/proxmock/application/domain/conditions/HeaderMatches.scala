package com.sk.app.proxmock.application.domain.conditions

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
  * Created by Szymon on 22.05.2016.
  */
case class HeaderMatches(header: String, pattern: String) extends Condition {
  override def test(message: Message[Object], context: ConfigurationContext): Boolean = {
    val headerValue = message.getHeaders.getOrDefault(header, "").toString // headers value are alweys of type String
    headerValue.matches(pattern)
  }
}
