package com.sk.app.proxmock.application.domain.providers.body
import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import groovy.lang.{Binding, GroovyShell}
import org.springframework.messaging.Message
import org.springframework.util.StringUtils._

/**
  * Created by szymo on 29/11/2016.
  */
case class GroovyExpressionBodyProvider(expression: String) extends BodyProvider {
  require(hasText(expression), "expression in GroovyExpressionBodyProvider cannot be blank")

  override def get(context: ConfigurationContext, message: Message[Object]): String = {
    val binding = new Binding()
    binding.setVariable("message", message)

    Option(new GroovyShell(binding).evaluate(expression))
      .map(_.toString)
      .orNull
  }
}

object GroovyExpressionBodyProvider {
  @JsonCreator
  def create(expression: String): GroovyExpressionBodyProvider =
    GroovyExpressionBodyProvider(expression)
}
