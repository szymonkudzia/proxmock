package com.sk.app.proxmock.application.domain.providers.headers

import com.fasterxml.jackson.annotation.{JsonCreator, JsonIgnoreProperties}
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import groovy.lang.{Binding, GroovyShell}
import org.springframework.messaging.Message

import scala.collection.JavaConverters._

/**
 * Created by Szymon on 04.10.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
case class GroovyExpressionHeadersProvider(expression: String) extends HeadersProvider {

  override def get(context: ConfigurationContext, message: Message[Object]) = {
    val binding = new Binding()
    binding.setVariable("message", message)

    Option(new GroovyShell(binding).evaluate(expression))
      .filter(_.isInstanceOf[java.util.Map[String, String]])
      .map(_.asInstanceOf[java.util.Map[String, String]].asScala.toMap)
      .getOrElse(Map.empty)
  }
}

object GroovyExpressionHeadersProvider {
  @JsonCreator
  def create(expression: String): GroovyExpressionHeadersProvider =
    GroovyExpressionHeadersProvider(expression)
}



