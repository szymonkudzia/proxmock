package com.sk.app.proxmock.application.domain.actions

import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.application.domain.actions.helpers.GroovyExpressionConfiguration
import org.springframework.messaging.Message
import org.springframework.util.StringUtils._


/**
  * Created by Szymon on 22.05.2016.
  */
case class GroovyExpressionAction(expression: String) extends Action with GroovyExpressionConfiguration{
  require(hasText(expression), "expression in GroovyExpressionAction cannot be blank")

  override def getExpression(source: Message[Object], context: ConfigurationContext): String = expression
}

object GroovyExpressionAction {
  @JsonCreator
  def create(expression: String) = GroovyExpressionAction(expression)
}