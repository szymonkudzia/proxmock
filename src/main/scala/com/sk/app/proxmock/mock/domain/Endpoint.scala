package com.sk.app.proxmock.mock.domain

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty, JsonIgnore}
import com.sk.app.proxmock.toolset.serialization.Wrapper

/**
 * Created by Szymon on 20.05.2016.
 */
case class Endpoint @JsonIgnore() (
  path: String,
  @JsonIgnore private val _action: Action
) {

  @JsonCreator
  def this(
      @JsonProperty("path") path: String,
      @JsonProperty("action") action: Wrapper[Action]) = this(path, action())

  @JsonProperty("action")
  private val action = Wrapper(_action)
}

