package com.sk.app.proxmock.application.domain.actions

import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.application.domain.actions.helpers.GroovyExpressionConfiguration
import org.springframework.messaging.Message
import org.springframework.util.StringUtils._


/**
  * Created by Szymon on 22.05.2016.
  */
case class GroovyExpressionFromFileAction(path: String) extends Action with GroovyExpressionConfiguration {
  require(hasText(path), "path in GroovyExpressionAction cannot be blank")

  override def getExpression(source: Message[Object], context: ConfigurationContext): String =
    context.fileToString(path)
}

object GroovyExpressionFromFileAction {
  @JsonCreator
  def create(path: String) = GroovyExpressionFromFileAction(path)
}
