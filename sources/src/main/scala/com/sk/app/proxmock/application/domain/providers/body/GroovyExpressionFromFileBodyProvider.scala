package com.sk.app.proxmock.application.domain.providers.body

import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message
import org.springframework.util.StringUtils._

/**
  * Created by szymo on 29/11/2016.
  */
case class GroovyExpressionFromFileBodyProvider(path: String) extends BodyProvider {
  require(hasText(path), "path in GroovyExpressionFromFileBodyProvider cannot be blank")

  override def get(context: ConfigurationContext, message: Message[Object]): String =
    GroovyExpressionBodyProvider(context.fileToString(path)).get(context, message)
}

object GroovyExpressionFromFileBodyProvider {
  @JsonCreator
  def create(path: String): GroovyExpressionFromFileBodyProvider =
    GroovyExpressionFromFileBodyProvider(path)
}


