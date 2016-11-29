package com.sk.app.proxmock.application.domain.providers.statuscode

import java.util.Objects._

import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
 * Created by Szymon on 05.10.2016.
 */
case class StaticStatusCodeProvider(code: Integer) extends StatusCodeProvider {
  require(nonNull(code), "code in StaticStatusCodeProvider cannot be null")

  override def get(context: ConfigurationContext, message: Message[Object]): Int = code
}

object StaticStatusCodeProvider {
  @JsonCreator
  def create(code: Integer) = StaticStatusCodeProvider(code)
}