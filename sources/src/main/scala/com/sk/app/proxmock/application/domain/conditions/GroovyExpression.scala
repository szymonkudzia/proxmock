package com.sk.app.proxmock.application.domain.conditions
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import groovy.lang.{Binding, GroovyShell}
import org.springframework.messaging.Message

/**
  * Created by szymo on 23/11/2016.
  */
case class GroovyExpression(expression: String, expressionPath: String) extends Condition {

  def fetchScript(context: ConfigurationContext) =
    Option(expressionPath).map(context.fileToString).getOrElse(expression)

  override def test(message: Message[Object], context: ConfigurationContext): Boolean = {
    val script = fetchScript(context)
    val binding = new Binding()
    binding.setVariable("message", message)

    val value = new GroovyShell(binding).evaluate(script)

    if (value == null) return false
    if (value.isInstanceOf[java.lang.Boolean]) return value.asInstanceOf[java.lang.Boolean]
    if (value.isInstanceOf[java.lang.String]) return "true".equalsIgnoreCase(value.toString)

    true
  }
}
