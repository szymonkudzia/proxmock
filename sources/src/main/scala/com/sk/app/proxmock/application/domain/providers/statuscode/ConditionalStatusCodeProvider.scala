package com.sk.app.proxmock.application.domain.providers.statuscode

import java.util.Objects._

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.application.domain.conditions.Condition
import org.springframework.messaging.Message

/**
 * Created by Szymon on 06.10.2016.
 */
case class ConditionalStatusCodeProvider(
    condition: Condition,
    ifTrue: StatusCodeProvider,
    ifFalse: StatusCodeProvider
  ) extends StatusCodeProvider {

  requireNonNull(condition, "condition of conditional status code provider cannot be null")
  requireNonNull(ifTrue, "ifTrue of conditional status code provider cannot be null")

  override def get(context: ConfigurationContext, message: Message[Object]): Int =
    condition.test(message, context) match {
      case true => ifTrue.get(context, message)
      case false => Option(ifFalse).getOrElse(SuccessStatusCodeProvider()).get(context, message)
    }
}
