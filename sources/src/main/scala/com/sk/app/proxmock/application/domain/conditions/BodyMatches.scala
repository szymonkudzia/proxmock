package com.sk.app.proxmock.application.domain.conditions
import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
  * Created by szymo on 23/11/2016.
  */
case class BodyMatches(pattern: String) extends Condition {
  override def test(message: Message[Object], context: ConfigurationContext): Boolean =
    Option(message)
      .map(_.getPayload)
      .getOrElse("")
      .toString
      .matches(pattern)
}

object BodyMatches {
  @JsonCreator
  def create(pattern: String) = BodyMatches(pattern)
}