package com.sk.app.proxmock.application.domain.providers.headers

import java.util.Objects._

import com.fasterxml.jackson.annotation.{JsonCreator, JsonIgnoreProperties}
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
 * Created by Szymon on 04.10.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
case class StaticHeadersProvider(headers: Map[String, String]) extends HeadersProvider {
  require(nonNull(headers), "headers in StaticHeadersProvider cannot be null")

  override def get(context: ConfigurationContext, message: Message[Object]) =
    headers
}

object StaticHeadersProvider {
  @JsonCreator
  def create(headers: Map[String, Object]) =
    StaticHeadersProvider(headers map { case (k, v) => (k, v.toString) })
}