package com.sk.app.proxmock.application.domain.providers.statuscode

import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.toolset.serialization.Yaml
import org.springframework.messaging.Message

/**
 * Created by Szymon on 05.10.2016.
 */
case class StaticFromFileStatusCodeProvider(path: String) extends StatusCodeProvider {
  override def get(context: ConfigurationContext, message: Message[Object]): Int = {
    Yaml.parse(context.fileToString(path), classOf[Int])
  }
}

object StaticFromFileStatusCodeProvider {
  @JsonCreator
  def create(path: String): StaticFromFileStatusCodeProvider =
    StaticFromFileStatusCodeProvider(path)
}

