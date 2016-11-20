package com.sk.app.proxmock.application.domain

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.application.domain.actions.Action
import org.springframework.http.HttpMethod
import org.springframework.integration.dsl.support.Consumer
import org.springframework.integration.dsl.{HeaderEnricherSpec, IntegrationFlowBuilder}
import org.springframework.integration.http.inbound.{HttpRequestHandlingMessagingGateway, RequestMapping}
import org.springframework.integration.http.support.DefaultHttpHeaderMapper
import org.springframework.messaging.MessageChannel

/**
  * Created by Szymon on 20.05.2016.
  */
case class Endpoint(
                     path: String,
                     method: Option[HttpMethod],
                     action: Action
                   ) {

  def configure(context: ConfigurationContext): Unit = {
    validateEndpoint()

    val input = inputChannel(context)
    val output = outputChannel(context)

    registerHttpGateway(input, output, context)
    context.flowBuilder.channel(input)

    action.configure(context)

    val flow = context.flowBuilder.asInstanceOf[IntegrationFlowBuilder]
      .headerFilter("proxmock*,Transfer-Encoding", true) // remove helper headers and transfer-encoding which in case of proxmock will confuse browser (proxmock replays at once with all of the content)
      .enrichHeaders(recalculateContentLength)
      .channel(output)

    context.register(s"$path-flow", flow.get())
  }


  private def registerHttpGateway(
     input: MessageChannel, output: MessageChannel, context: ConfigurationContext) = {

    val mapper = new DefaultHttpHeaderMapper()
    mapper.setInboundHeaderNames(Array("*"))
    mapper.setOutboundHeaderNames(Array("*"))
    mapper.setUserDefinedHeaderPrefix(null)

    val http = new HttpRequestHandlingMessagingGateway(true)
    http.setAutoStartup(true)
    http.setRequestMapping(requestMapping())
    http.setRequestChannel(input)
    http.setReplyChannel(output)
    http.setErrorChannel(output)
    http.setHeaderMapper(mapper)

    context.httpGateway = context.register(s"$path-httpInputGateway", http)
  }

  private def requestMapping() = {
    val requestMapping = new RequestMapping()
    requestMapping.setPathPatterns(path)
    requestMapping.setMethods(method map (Array(_)) getOrElse HttpMethod.values(): _*)
    requestMapping
  }


  def recalculateContentLength = new Consumer[HeaderEnricherSpec] {
    override def accept(t: HeaderEnricherSpec): Unit =
      t.headerExpression("Content-Length", "payload instanceOf T(String) ? payload.length() : payload.length")
  }


  private def inputChannel(context: ConfigurationContext) =
    context.newDirectChannel(s"$path-inputChannel")


  private def outputChannel(context: ConfigurationContext) =
    context.newDirectChannel(s"$path-outputChannel")


  private def validateEndpoint() = {
    require(path != null, "Endpoint path must be not null!")
    require(action != null, "Endpoint's action must be specified!")
  }
}

