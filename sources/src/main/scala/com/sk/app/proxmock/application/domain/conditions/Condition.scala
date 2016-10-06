package com.sk.app.proxmock.application.domain.conditions

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import org.springframework.messaging.Message

/**
 * Created by Szymon on 20.05.2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(Array(
  new Type(value = classOf[HeaderEquals], name="headerEquals"),
  new Type(value = classOf[RandomCondition], name="random")
))
abstract class Condition {
  def test(message: Message[Object]): Boolean
}

