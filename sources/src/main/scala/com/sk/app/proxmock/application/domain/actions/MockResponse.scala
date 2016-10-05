package com.sk.app.proxmock.application.domain.actions

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.application.domain.providers.body.BodyProvider
import com.sk.app.proxmock.application.domain.providers.headers.HeadersProvider
import com.sk.app.proxmock.application.domain.providers.statuscode.StatusCodeProvider
import org.springframework.integration.http.HttpHeaders._
import org.springframework.integration.support.MessageBuilder
import org.springframework.integration.transformer.GenericTransformer
import org.springframework.messaging.Message

import scala.collection.JavaConverters._


/**
 * Created by Szymon on 22.05.2016.
 */
case class MockResponse
(
  statusCode: StatusCodeProvider,
  headers: HeadersProvider,
  body: BodyProvider
) extends Action {

  override def configure(context: ConfigurationContext): Unit = {
      context
        .flowBuilder
        .transform(new Transformer(context))
  }

  private def fetchHeaders(context: ConfigurationContext, message: Message[Object]) =
    Option(headers) map (_.get(context, message)) getOrElse Map()

  private def fetchBody(context: ConfigurationContext, message: Message[Object]): String =
    Option(body) map (_.get(context, message)) getOrElse ""

  private def fetchStatusCode(context: ConfigurationContext, message: Message[Object]): Int =
    Option(statusCode) map (_.get(context, message)) getOrElse 200



  private class Transformer(context: ConfigurationContext)
    extends GenericTransformer[Message[Object], Message[String]] {

    override def transform(source: Message[Object]): Message[String] = {
      val headers = fetchHeaders(context, source)
      val body = fetchBody(context, source)
      val statusCode = fetchStatusCode(context, source)

      headers.keys foreach context.addOutboundHeaderPattern

      MessageBuilder
        .withPayload(body)
        .copyHeaders(source.getHeaders)
        .copyHeaders(headers.asJava)
        .setHeader(STATUS_CODE, statusCode)
        .build()
    }
  }
}