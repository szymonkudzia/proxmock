package com.sk.app.proxmock.application.domain.conditions

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
 * Created by Szymon on 22.05.2016.
 */
case class AlwaysFalseCondition() extends Condition {
  override def test(message: Message[Object], context: ConfigurationContext): Boolean = false
}
