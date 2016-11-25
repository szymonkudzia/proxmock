package com.sk.app.proxmock.application.domain.conditions

import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
  * Created by szymo on 23/11/2016.
  */
case class GroovyExpressionFile(expressionPath: String) extends Condition {

  override def test(message: Message[Object], context: ConfigurationContext): Boolean = {
    GroovyExpression(context.fileToString(expressionPath)).test(message, context)
  }
}

object GroovyExpressionFile {
  @JsonCreator
  def create(expression: String): GroovyExpressionFile = GroovyExpressionFile(expression)
}
