package com.sk.app.proxmock.application.domain.providers.body

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
 * Created by Szymon on 04.10.2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(Array(
  new Type(value = classOf[EmptyBodyProvider], name="empty"),
  new Type(value = classOf[StaticBodyProvider], name="static"),
  new Type(value = classOf[StaticBodyFromFileProvider], name="staticFromFile"),
  new Type(value = classOf[GroovyExpressionBodyProvider], name="groovyExpression"),
  new Type(value = classOf[GroovyExpressionFromFileBodyProvider], name="groovyExpressionFromFile"),
  new Type(value = classOf[ConditionalBodyProvider], name="conditional")
))
abstract class BodyProvider {
  def get(context: ConfigurationContext, message: Message[Object]): String
}
