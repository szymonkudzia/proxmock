package com.sk.app.proxmock.application.domain.actions.mock

import java.io.File

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.application.domain.actions.Action
import org.apache.commons.io.FileUtils
import org.springframework.integration.support.MessageBuilder
import org.springframework.integration.transformer.GenericTransformer
import org.springframework.messaging.Message

import scala.collection.JavaConverters._


/**
 * Created by Szymon on 22.05.2016.
 */
case class StaticMockResponse
(
  headers: Option[Map[String, String]],
  headersPath: Option[String],
  bodyContent: Option[String],
  bodyPath: Option[String]
) extends Action {

  override def configure(context: ConfigurationContext): Unit = {
    val headers = fetchHeaders()
    val body = fetchBody(context.configRootDir)

    context
      .flowBuilder
      .transform(Transformer(headers, body))
  }


  private def fetchHeaders(): Map[String, String] = {
    Map()
  }


  private def fetchBody(confRootDir: String): String =
    bodyContent.getOrElse(bodyPath.map(fileToString(_, confRootDir)).get)


  private def fileToString(path: String, parentDir: String) = {
    var file = new File(path)
    if (!file.exists()) file = new File(parentDir, path)
    
    FileUtils.readFileToString(file)
  }  
}



private class Transformer
  (headers: Map[String, String], body: String)
    extends GenericTransformer[Message[String], Message[String]] {

  override def transform(source: Message[String]): Message[String] =
    MessageBuilder
      .withPayload(body)
      .copyHeaders(source.getHeaders)
      .copyHeaders(headers.asJava)
      .build()
}

private object Transformer {
  def apply(headers: Map[String, String], body: String) = new Transformer(headers, body)
}
