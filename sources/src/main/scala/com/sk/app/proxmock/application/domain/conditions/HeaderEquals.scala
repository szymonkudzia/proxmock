package com.sk.app.proxmock.application.domain.conditions

import org.springframework.messaging.Message

/**
 * Created by Szymon on 22.05.2016.
 */
case class HeaderEquals(
   headerName: String,
   headerValue: String
 ) extends Condition {
  override def test(message: Message[Object]): Boolean = false
}
