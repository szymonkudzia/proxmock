package com.sk.app.proxmock.application.domain.actions

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation._
import com.sk.app.proxmock.application.configuration.ConfigurationContext

/**
 * Created by Szymon on 20.05.2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(Array(
  new Type(value = classOf[FirstMetCondition], name="firstMetCondition"),
  new Type(value = classOf[MockResponse], name="mockResponse"),
  new Type(value = classOf[Proxy], name="proxy"),
  new Type(value = classOf[ConditionalAction], name="conditional")
))
abstract class Action {
  def configure(context: ConfigurationContext)
}

