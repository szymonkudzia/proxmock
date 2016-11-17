package com.sk.app.proxmock.application.domain.actions

import java.util

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.slf4j.LoggerFactory
import org.springframework.expression.Expression
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.http.client.ClientHttpResponse
import org.springframework.integration.dsl.http.Http
import org.springframework.integration.transformer.GenericTransformer
import org.springframework.messaging.Message
import org.springframework.web.client.ResponseErrorHandler


/**
  * Created by Szymon on 22.05.2016.
  */
case class Proxy
(
  toUrl: String
) extends Action {
  val parser = new SpelExpressionParser()

  override def configure(context: ConfigurationContext): Unit = {
    updateEndpointPathWithAcceptingAllRequestPattern(context)

    context.flowBuilder
      .handle(Http.outboundGateway(endpointUrl)
        .expectedResponseType(classOf[String])
        .mappedRequestHeaders("*")
        .mappedResponseHeaders("*")
        .httpMethodExpression(parser.parseExpression("headers.http_requestMethod"))
        .extractPayload(true)
        .transferCookies(true)
        .errorHandler(new IgnoreErrorResponse())
        .uriVariable("anyPath", parser.parseExpression("headers.anyPath"))
        .get())
  }

  def updateEndpointPathWithAcceptingAllRequestPattern(context: ConfigurationContext) = {
    val paths = context.httpGateway.getRequestMapping.getPathPatterns
    val updatedPaths = paths.map(p => appendAcceptingAllRequestPattern(p))

    context.httpGateway.getRequestMapping.setPathPatterns(updatedPaths: _*)

    val headers = new util.HashMap[String, Expression]()
    headers.put("anyPath", parser.parseExpression("#pathVariables.anyPath"))

    context.httpGateway.setHeaderExpressions(headers)
  }

  def endpointUrl: String = appendAcceptingAllRequestPattern(toUrl)

  def appendAcceptingAllRequestPattern(url: String): String =
    if (url.endsWith("{anyPath}"))
      url
    else if (url.endsWith("/"))
      s"$url{anyPath}"
    else
      s"$url/{anyPath}"

  private class IgnoreErrorResponse extends ResponseErrorHandler {
    override def hasError(response: ClientHttpResponse): Boolean = false

    override def handleError(response: ClientHttpResponse): Unit = {}
  }
}