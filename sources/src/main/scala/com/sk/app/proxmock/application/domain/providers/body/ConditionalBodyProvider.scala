package com.sk.app.proxmock.application.domain.providers.body

import java.util.Objects._

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.application.domain.conditions.Condition
import org.springframework.messaging.Message

/**
 * Created by Szymon on 06.10.2016.
 */
case class ConditionalBodyProvider(
    condition: Condition,
    ifTrue: BodyProvider,
    ifFalse: BodyProvider
  ) extends BodyProvider {

  requireNonNull(condition, "condition of conditional body provider cannot be null")
  requireNonNull(ifTrue, "ifTrue of conditional body provider cannot be null")

  override def get(context: ConfigurationContext, message: Message[Object]): String =
    condition.test(message) match {
      case true => ifTrue.get(context, message)
      case false => Option(ifFalse).getOrElse(EmptyBodyProvider()).get(context, message)
    }
}
