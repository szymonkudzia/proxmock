package com.sk.app.proxmock.application.domain.conditions
import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import groovy.lang.{Binding, GroovyShell}
import org.springframework.messaging.Message

/**
  * Created by szymo on 23/11/2016.
  */
case class GroovyExpression(expression: String) extends Condition {

  override def test(message: Message[Object], context: ConfigurationContext): Boolean = {
    val binding = new Binding()
    binding.setVariable("message", message)

    val value = new GroovyShell(binding).evaluate(expression)

    if (value == null) return false
    if (value.isInstanceOf[java.lang.Boolean]) return value.asInstanceOf[java.lang.Boolean]
    if (value.isInstanceOf[java.lang.String]) return !"false".equalsIgnoreCase(value.toString)

    true
  }
}

object GroovyExpression {
  @JsonCreator
  def create(expression: String) = GroovyExpression(expression)
}
