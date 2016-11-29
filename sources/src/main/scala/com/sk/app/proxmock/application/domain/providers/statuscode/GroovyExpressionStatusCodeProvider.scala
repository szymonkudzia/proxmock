package com.sk.app.proxmock.application.domain.providers.statuscode

import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import groovy.lang.{Binding, GroovyShell}
import org.springframework.messaging.Message

/**
  * Created by Szymon on 05.10.2016.
  */
case class GroovyExpressionStatusCodeProvider(expression: String) extends StatusCodeProvider {
  override def get(context: ConfigurationContext, message: Message[Object]): Int = {
    val binding = new Binding()
    binding.setVariable("message", message)

    Option(new GroovyShell(binding).evaluate(expression))
      .filter(_.isInstanceOf[Integer])
      .map(_.asInstanceOf[Integer].toInt)
      .getOrElse(500)
  }
}

object GroovyExpressionStatusCodeProvider {
  @JsonCreator
  def create(expression: String): GroovyExpressionStatusCodeProvider =
    GroovyExpressionStatusCodeProvider(expression)
}


