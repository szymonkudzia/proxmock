package com.sk.app.proxmock.application.domain.providers.body

import com.fasterxml.jackson.annotation.{JsonCreator, JsonIgnoreProperties}
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
 * Created by Szymon on 04.10.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
case class StaticBodyProvider(body: String) extends BodyProvider {

  override def get(context: ConfigurationContext, message: Message[Object]): String = body
}

object StaticBodyProvider {
  @JsonCreator
  def create(body: Object) = StaticBodyProvider(body.toString)
}