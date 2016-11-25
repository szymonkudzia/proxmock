package com.sk.app.proxmock.application.domain.conditions

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
  * Created by Szymon on 22.05.2016.
  */
case class HeaderMatches(header: String, pattern: String) extends Condition {
  override def test(message: Message[Object], context: ConfigurationContext): Boolean = {
    val headerValue = Option(message.getHeaders.get(header))
      .orElse(Option(message.getHeaders.get(header.toLowerCase)))
      .map(_.toString)
      .getOrElse("")

    headerValue.matches(pattern)
  }
}
