package com.sk.app.proxmock.application.domain.providers.url

import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message
import org.springframework.util.StringUtils._

/**
  * Created by szymo on 19/11/2016.
  */
case class GroovyExpressionFromFileUrlProvider(path: String) extends UrlProvider {
  require(hasText(path), "path in GroovyExpressionFromFileUrlProvider cannot be blank")

  override def get(context: ConfigurationContext, message: Message[Object]): String =
    GroovyExpressionUrlProvider(context.fileToString(path)).get(context, message)
}

object GroovyExpressionFromFileUrlProvider {
  @JsonCreator
  def create(path: String) = GroovyExpressionFromFileUrlProvider(path)
}





