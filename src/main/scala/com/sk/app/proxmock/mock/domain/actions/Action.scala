package com.sk.app.proxmock.mock.domain.actions

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation._
import com.sk.app.proxmock.mock.domain.actions.mock.StaticMockResponse

/**
 * Created by Szymon on 20.05.2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(Array(
  new Type(value = classOf[FirstMetCondition], name="firstMetCondition"),
  new Type(value = classOf[StaticMockResponse], name="staticMockResponse")
))
abstract class Action {

}

