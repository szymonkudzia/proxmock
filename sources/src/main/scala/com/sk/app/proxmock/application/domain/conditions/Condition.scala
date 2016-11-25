package com.sk.app.proxmock.application.domain.conditions

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
 * Created by Szymon on 20.05.2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(Array(
  new Type(value = classOf[HeaderMatches], name="headerMatches"),
  new Type(value = classOf[UriMatches], name="uriMatches"),
  new Type(value = classOf[BodyMatches], name="bodyMatches"),
  new Type(value = classOf[RandomCondition], name="random"),
  new Type(value = classOf[AlwaysTrueCondition], name="alwaysTrue"),
  new Type(value = classOf[AlwaysFalseCondition], name="alwaysFalse"),
  new Type(value = classOf[GroovyExpression], name="groovyExpression"),
  new Type(value = classOf[GroovyExpressionFile], name="groovyExpressionFile")
))
abstract class Condition {
  def test(message: Message[Object], context: ConfigurationContext): Boolean
}

