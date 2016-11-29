package com.sk.app.proxmock.application.domain.providers.url

import java.util.Objects._

import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import groovy.lang.{Binding, GroovyShell}
import org.springframework.messaging.Message
import org.springframework.util.StringUtils._

/**
  * Created by szymo on 19/11/2016.
  */
case class GroovyExpressionUrlProvider(expression: String) extends UrlProvider {
  require(hasText(expression), "expression in GroovyExpressionUrlProvider cannot be blank")

  override def get(context: ConfigurationContext, message: Message[Object]): String = {
    val binding = new Binding()
    binding.setVariable("message", message)

    val url = Option(new GroovyShell(binding).evaluate(expression))
      .map(_.toString)
      .orNull

    requireNonNull(url, "groovyExpression cannot return nullable url")
  }
}

object GroovyExpressionUrlProvider {
  @JsonCreator
  def create(expression: String) = GroovyExpressionUrlProvider(expression)
}


