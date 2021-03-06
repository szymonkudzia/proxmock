package com.sk.app.proxmock.application.domain.providers.headers

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
 * Created by Szymon on 04.10.2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(Array(
  new Type(value = classOf[EmptyHeadersProvider], name="empty"),
  new Type(value = classOf[StaticHeadersProvider], name="static"),
  new Type(value = classOf[StaticFromFileHeadersProvider], name="staticFromFile"),
  new Type(value = classOf[GroovyExpressionHeadersProvider], name="groovyExpression"),
  new Type(value = classOf[GroovyExpressionFromFileHeadersProvider], name="groovyExpressionFromFile"),
  new Type(value = classOf[ConditionalHeadersProvider], name="conditional")
))
abstract class HeadersProvider {
  def get(context: ConfigurationContext, message: Message[Object]): Map[String, String]
}
