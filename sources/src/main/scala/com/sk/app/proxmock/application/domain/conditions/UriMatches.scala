package com.sk.app.proxmock.application.domain.conditions

import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
  * Created by szymo on 23/11/2016.
  */
case class UriMatches(pattern: String) extends Condition {
  val headerMatches = HeaderMatches("http_requestUrl", pattern)

  override def test(message: Message[Object], context: ConfigurationContext): Boolean =
    headerMatches.test(message, context)
}

object UriMatches {
  @JsonCreator
  def create(pattern: String) = UriMatches(pattern)
}