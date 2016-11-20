package com.sk.app.proxmock.application.domain.providers.url

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
  * Created by szymo on 19/11/2016.
  */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(Array(
  new Type(value = classOf[StaticUrlProvider], name="static")
))
abstract class UrlProvider {
  def get(context: ConfigurationContext, message: Message[Object]): String
}
