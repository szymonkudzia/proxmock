package com.sk.app.proxmock.mock.domain

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}

/**
 * Created by Szymon on 20.05.2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(Array(
  new Type(value = classOf[HeaderEquals], name="headerEquals")
))
abstract class Condition {

}

case class HeaderEquals(
  headerName: String,
  headerValue: String
) extends Condition