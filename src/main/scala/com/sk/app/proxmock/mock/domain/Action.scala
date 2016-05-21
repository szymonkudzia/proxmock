package com.sk.app.proxmock.mock.domain

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation._
import com.sk.app.proxmock.toolset.serialization.Wrapper

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


case class ConditionalAction @JsonIgnore() (
  condition: Condition,
  @JsonIgnore private val _action: Action
) {

  @JsonCreator
  def this(
            @JsonProperty("condition") condition: Condition,
            @JsonProperty("action") action: Wrapper[Action]) = this(condition, action())

  @JsonProperty("action")
  private val action = Wrapper(_action)
}


case class StaticMockResponseAction(
  headers: Map[String, String],
  bodyContent: String,
  bodyPath: String
) extends Action