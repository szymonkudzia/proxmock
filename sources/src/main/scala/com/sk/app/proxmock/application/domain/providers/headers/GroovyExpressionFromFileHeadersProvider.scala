package com.sk.app.proxmock.application.domain.providers.headers

import com.fasterxml.jackson.annotation.{JsonCreator, JsonIgnoreProperties}
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message
import org.springframework.util.StringUtils._

/**
 * Created by Szymon on 04.10.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
case class GroovyExpressionFromFileHeadersProvider(path: String) extends HeadersProvider {
  require(hasText(path), "path in GroovyExpressionFromFileHeadersProvider cannot be blank")

  override def get(context: ConfigurationContext, message: Message[Object]) = {
    GroovyExpressionHeadersProvider(context.fileToString(path)).get(context, message)
  }
}


object GroovyExpressionFromFileHeadersProvider {
  @JsonCreator
  def create(path: String): GroovyExpressionFromFileHeadersProvider =
    GroovyExpressionFromFileHeadersProvider(path)
}




