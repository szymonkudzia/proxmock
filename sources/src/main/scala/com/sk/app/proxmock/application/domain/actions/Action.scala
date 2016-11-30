package com.sk.app.proxmock.application.domain.actions

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation._
import com.sk.app.proxmock.application.configuration.ConfigurationContext

/**
 * Created by Szymon on 20.05.2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(Array(
  new Type(value = classOf[FirstMetConditionAction], name="firstMetCondition"),
  new Type(value = classOf[MockResponseAction], name="mockResponse"),
  new Type(value = classOf[ProxyAction], name="proxy"),
  new Type(value = classOf[ConditionalAction], name="conditional"),
  new Type(value = classOf[GroovyExpressionAction], name="groovyExpression"),
  new Type(value = classOf[GroovyExpressionFromFileAction], name="groovyExpressionFromFile")
))
abstract class Action {
  def configure(context: ConfigurationContext)
}

