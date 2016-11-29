package com.sk.app.proxmock.application.domain.providers.headers

import com.fasterxml.jackson.annotation.{JsonCreator, JsonIgnoreProperties}
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.toolset.serialization.Yaml
import org.springframework.messaging.Message

/**
 * Created by Szymon on 04.10.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
case class StaticFromFileHeadersProvider(path: String) extends HeadersProvider {

  override def get(context: ConfigurationContext, message: Message[Object]) =
    Yaml.parse(context.fileToString(path), classOf[Map[String, String]])
}

object StaticFromFileHeadersProvider {
  @JsonCreator
  def create(path: String): StaticFromFileHeadersProvider = StaticFromFileHeadersProvider(path)
}

