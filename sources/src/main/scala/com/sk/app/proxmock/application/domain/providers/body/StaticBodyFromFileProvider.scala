package com.sk.app.proxmock.application.domain.providers.body
import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message
import org.springframework.util.StringUtils

/**
  * Created by szymo on 29/11/2016.
  */
case class StaticBodyFromFileProvider(path: String) extends  BodyProvider{
  require(StringUtils.hasText(path), "path in StaticBodyFileProvider cannot be blank")

  override def get(context: ConfigurationContext, message: Message[Object]): String =
    StaticBodyProvider(context.fileToString(path)).get(context, message)
}

object StaticBodyFromFileProvider {
  @JsonCreator
  def create(path: String): StaticBodyFromFileProvider = StaticBodyFromFileProvider(path)
}