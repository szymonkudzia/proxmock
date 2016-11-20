package com.sk.app.proxmock.application.domain.actions

import java.util

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.application.domain.providers.url.UrlProvider
import org.springframework.expression.Expression
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.http.client.ClientHttpResponse
import org.springframework.integration.dsl.http.Http
import org.springframework.integration.support.MessageBuilder
import org.springframework.integration.transformer.GenericTransformer
import org.springframework.messaging.Message
import org.springframework.web.client.ResponseErrorHandler


/**
  * Created by Szymon on 22.05.2016.
  */
case class Proxy(toUrl: UrlProvider) extends Action {
  val parser = new SpelExpressionParser()

  override def configure(context: ConfigurationContext): Unit = {
    updateEndpointPathWithAcceptingAllRequestPattern(context)
    storeRequestUriInHeaders(context)

    context.flowBuilder
      .transform(new AddHeaderWithDestinationUrl(context))

    // I got compilation error when I wanted attach .handle just after .transform(...) - God why? Whats wrong with scala?
    context.flowBuilder
      .handle(Http.outboundGateway(parser.parseExpression("headers.proxmock_dest_url"))
        .expectedResponseType(classOf[Array[Byte]])
        .mappedRequestHeaders("*")
        .mappedResponseHeaders("*")
        .httpMethodExpression(expression("headers.http_requestMethod"))
        .extractPayload(true)
        .transferCookies(true)
        .errorHandler(new IgnoreErrorResponse())
        .uriVariable("proxmock_anyPath", expression("headers.proxmock_anyPath"))
        .get())
  }

  def expression(raw: String): Expression = {
    parser.parseExpression(raw)
  }

  def updateEndpointPathWithAcceptingAllRequestPattern(context: ConfigurationContext) = {
    val paths = context.httpGateway.getRequestMapping.getPathPatterns
    val updatedPaths = paths.map(p => appendAcceptingAllRequestPattern(p))

    context.httpGateway.getRequestMapping.setPathPatterns(paths ++ updatedPaths: _*)
  }

  def storeRequestUriInHeaders(context: ConfigurationContext): Unit = {
    val headers = new util.HashMap[String, Expression]()
    headers.put("proxmock_anyPath", expression("#pathVariables != null ? #pathVariables.proxmock_anyPath : ''"))

    context.httpGateway.setHeaderExpressions(headers)
  }

  def appendAcceptingAllRequestPattern(url: String): String =
    if (url.endsWith("{proxmock_anyPath}"))
      url
    else if (url.endsWith("/"))
      s"$url{proxmock_anyPath}"
    else
      s"$url/{proxmock_anyPath}"


  private class AddHeaderWithDestinationUrl(context: ConfigurationContext)
    extends GenericTransformer[Message[Object], Message[Object]] {

    override def transform(source: Message[Object]): Message[Object] = MessageBuilder
      .fromMessage(source)
      .setHeader("proxmock_dest_url", appendAcceptingAllRequestPattern(toUrl.get(context, source)))
      .build()
  }

  private class IgnoreErrorResponse extends ResponseErrorHandler {
    override def hasError(response: ClientHttpResponse): Boolean = false

    override def handleError(response: ClientHttpResponse): Unit = {}
  }
}