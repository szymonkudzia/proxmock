package com.sk.app.proxmock.application.domain.providers.statuscode

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
 * Created by Szymon on 04.10.2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(Array(
  new Type(value = classOf[SuccessStatusCodeProvider], name="success"),
  new Type(value = classOf[StaticStatusCodeProvider], name="static"),
  new Type(value = classOf[ConditionalStatusCodeProvider], name="conditional")
))
abstract class StatusCodeProvider {
  def get(context: ConfigurationContext, message: Message[Object]): Int
}
