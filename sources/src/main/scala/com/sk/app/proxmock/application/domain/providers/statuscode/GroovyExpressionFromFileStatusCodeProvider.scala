package com.sk.app.proxmock.application.domain.providers.statuscode

import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import org.springframework.messaging.Message

/**
  * Created by Szymon on 05.10.2016.
  */
case class GroovyExpressionFromFileStatusCodeProvider(path: String) extends StatusCodeProvider {
  override def get(context: ConfigurationContext, message: Message[Object]): Int =
    GroovyExpressionStatusCodeProvider(context.fileToString(path)).get(context, message)
}


object GroovyExpressionFromFileStatusCodeProvider {
  @JsonCreator
  def create(path: String): GroovyExpressionFromFileStatusCodeProvider =
    GroovyExpressionFromFileStatusCodeProvider(path)
}






