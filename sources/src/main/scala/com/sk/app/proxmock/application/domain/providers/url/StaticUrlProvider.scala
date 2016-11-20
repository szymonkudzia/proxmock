package com.sk.app.proxmock.application.domain.providers.url
import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
  * Created by szymo on 19/11/2016.
  */
case class StaticUrlProvider(url: String) extends UrlProvider {
  override def get(context: ConfigurationContext, message: Message[Object]): String = url
}

object StaticUrlProvider {
  @JsonCreator
  def create(url: String) = StaticUrlProvider(url)
}
