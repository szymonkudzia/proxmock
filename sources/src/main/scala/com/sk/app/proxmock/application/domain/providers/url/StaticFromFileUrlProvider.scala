package com.sk.app.proxmock.application.domain.providers.url

import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message
import org.springframework.util.StringUtils._

/**
  * Created by szymo on 19/11/2016.
  */
case class StaticFromFileUrlProvider(path: String) extends UrlProvider {
  require(hasText(path), "path in StaticFromFileUrlProvider cannot be blank")

  override def get(context: ConfigurationContext, message: Message[Object]): String =
    context.fileToString(path)
}

object StaticFromFileUrlProvider {
  @JsonCreator
  def create(path: String) = StaticFromFileUrlProvider(path)
}
