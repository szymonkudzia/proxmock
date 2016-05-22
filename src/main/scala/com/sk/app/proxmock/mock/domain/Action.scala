package com.sk.app.proxmock.mock.domain

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation._

/**
 * Created by Szymon on 20.05.2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(Array(
  new Type(value = classOf[ActionPicker], name="pickFirstTrue"),
  new Type(value = classOf[StaticMockResponseAction], name="staticMockResponse")
))
abstract class Action {

}


case class ActionPicker(
  conditionalActions: List[ConditionalAction]
) extends Action


case class ConditionalAction(
  condition: Condition,
  action: Action
)


case class StaticMockResponseAction(
  headers: Map[String, String],
  bodyContent: String,
  bodyPath: String
) extends Action