package com.sk.app.proxmock.application.domain.providers.headers

import java.util.Objects._

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.application.domain.conditions.Condition
import org.springframework.messaging.Message

/**
 * Created by Szymon on 06.10.2016.
 */
case class ConditionalHeadersProvider(
    condition: Condition,
    ifTrue: HeadersProvider,
    ifFalse: HeadersProvider
  ) extends HeadersProvider {

  requireNonNull(condition, "condition of conditional headers provider cannot be null")
  requireNonNull(ifTrue, "ifTrue of conditional headers provider cannot be null")

  override def get(context: ConfigurationContext, message: Message[Object]): Map[String, String] =
    condition.test(message) match {
      case true => ifTrue.get(context, message)
      case false => Option(ifFalse).getOrElse(EmptyHeadersProvider()).get(context, message)
    }
}
