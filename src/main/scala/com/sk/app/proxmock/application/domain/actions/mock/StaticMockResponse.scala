package com.sk.app.proxmock.application.domain.actions.mock

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.application.domain.actions.Action
import com.sk.app.proxmock.toolset.serialization.Yaml
import org.springframework.integration.support.MessageBuilder
import org.springframework.integration.transformer.GenericTransformer
import org.springframework.messaging.Message

import scala.collection.JavaConverters._


/**
 * Created by Szymon on 22.05.2016.
 */
case class StaticMockResponse
(
  headers: Option[Map[String, Object]],
  headersPath: Option[String],
  bodyContent: Option[String],
  bodyPath: Option[String]
) extends Action {

  override def configure(context: ConfigurationContext): Unit = {
    val bodyProvider = () => fetchBody(context)

    context
      .flowBuilder
      .transform(Transformer(headersProvider(context), bodyProvider))
  }

  private def headersProvider(context: ConfigurationContext) = () => {
    val headers = fetchHeaders(context)
    headers.keys foreach context.addOutboundHeaderPattern
    headers
  }


  private def fetchHeaders(context: ConfigurationContext): Map[String, Object] =
    headers.getOrElse(Map()) ++ headersPath.map(fileToMap(_, context)).getOrElse(Map())

  private def fetchBody(context: ConfigurationContext): String =
    bodyContent.getOrElse(bodyPath.map(context.fileToString).get)

  private def fileToMap(path: String, context: ConfigurationContext): Map[String, String] =
    Yaml.parse(context.fileToString(path), classOf[Map[String, String]])
}



private class Transformer
  (headersProvider: () => Map[String, Object],
   bodyProvider: () => String)
    extends GenericTransformer[Message[String], Message[String]] {

  override def transform(source: Message[String]): Message[String] =
    MessageBuilder
      .withPayload(bodyProvider())
      .copyHeaders(source.getHeaders)
      .copyHeaders(headersProvider().asJava)
      .build()
}

private object Transformer {
  def apply(
     headersProvider: () => Map[String, Object],
     bodyProvider: () => String) =
    new Transformer(headersProvider, bodyProvider)
}
