package com.sk.app.proxmock.application.domain.actions.mock

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.application.domain.actions.Action
import com.sk.app.proxmock.toolset.serialization.Yaml
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.integration.support.MessageBuilder
import org.springframework.integration.transformer.GenericTransformer
import org.springframework.messaging.Message

import scala.collection.JavaConverters._


/**
 * Created by Szymon on 22.05.2016.
 */
case class StaticMockResponse
(
  statusCode: Option[String],
  statusCodePath: Option[String],
  headers: Option[Map[String, String]],
  headersPath: Option[String],
  bodyContent: Option[String],
  bodyPath: Option[String],
  definitionPath: Option[String]
) extends Action {

  override def configure(context: ConfigurationContext): Unit = definitionPath match {
    case Some(path) =>
      val staticMockResponse = Yaml.parse(context.fileToString(path), classOf[StaticMockResponse])
      staticMockResponse.configure(context)

    case None =>
      context
        .flowBuilder
        .transform(new Transformer(context))
  }

  private def fetchHeaders(context: ConfigurationContext): Map[String, String] =
    headers.getOrElse(Map()) ++ headersPath.map(context.fileToMap).getOrElse(Map())

  private def fetchBody(context: ConfigurationContext): String =
    bodyContent.getOrElse(bodyPath.map(context.fileToString).get)

  private def fetchStatusCode(context: ConfigurationContext): String =
    statusCodePath.map(context.fileToString).getOrElse(statusCode.getOrElse("200"))



  private class Transformer(context: ConfigurationContext)
    extends GenericTransformer[Message[Object], Message[String]] {

    override def transform(source: Message[Object]): Message[String] = {
      val headers = fetchHeaders(context)
      headers.keys foreach context.addOutboundHeaderPattern

      val statusCode = fetchStatusCode(context)
      val parser = new SpelExpressionParser()
      context.httpGateway.setStatusCodeExpression(parser.parseRaw(statusCode))

      MessageBuilder
        .withPayload(fetchBody(context))
        .copyHeaders(source.getHeaders)
        .copyHeaders(headers.asJava)
        .build()
    }
  }
}