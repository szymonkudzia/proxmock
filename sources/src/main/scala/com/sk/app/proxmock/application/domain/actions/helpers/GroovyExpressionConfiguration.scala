package com.sk.app.proxmock.application.domain.actions.helpers

import java.util.Objects._

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import groovy.lang.{Binding, GroovyShell}
import org.springframework.integration.IntegrationMessageHeaderAccessor
import org.springframework.integration.support.MessageBuilder
import org.springframework.integration.transformer.GenericTransformer
import org.springframework.messaging.{Message, MessageChannel}

/**
  * Created by szymo on 30/11/2016.
  */
trait GroovyExpressionConfiguration {
  def getExpression(source: Message[Object], context: ConfigurationContext): String

  def configure(context: ConfigurationContext): Unit = {
    context
      .flowBuilder
      .transform(new Transformer(context))
  }

  private class Transformer(context: ConfigurationContext)
    extends GenericTransformer[Message[Object], Message[Object]] {

    override def transform(source: Message[Object]): Message[Object] = {
      val binding = new Binding()
      binding.setVariable("source", source)

      val response = Option(new GroovyShell(binding).evaluate(getAdjustedExpression(source, context)))
        .filter(_.isInstanceOf[Message[Object]])
        .map(_.asInstanceOf[Message[Object]])
        .orNull

      requireNonNull(response, "GroovyExpression cannot produce null message object")

      val accessor = new IntegrationMessageHeaderAccessor(source)

      MessageBuilder
        .fromMessage(response)
        .setErrorChannel(accessor.getErrorChannel.asInstanceOf[MessageChannel])
        .setReplyChannel(accessor.getReplyChannel.asInstanceOf[MessageChannel])
        .build()
    }
  }

  def getAdjustedExpression(source: Message[Object], context: ConfigurationContext): String = s"""
       |import org.springframework.integration.support.MessageBuilder
       |import org.springframework.messaging.Message
       |${getExpression(source, context)}
     """.stripMargin

}
