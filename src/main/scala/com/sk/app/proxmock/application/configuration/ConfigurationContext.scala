package com.sk.app.proxmock.application.configuration

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.integration.dsl.IntegrationFlowBuilder
import org.springframework.integration.dsl.channel.MessageChannels

/**
 * Created by Szymon on 26.05.2016.
 */
class ConfigurationContext private (
  val springContext: ConfigurableApplicationContext,
  val flowBuilder: IntegrationFlowBuilder,
  val configRootDir: String
) {

  def register[T](beanName: String, bean: T): T = {
    val initializedBean = springContext.getAutowireCapableBeanFactory.initializeBean(bean, beanName)
    springContext.getBeanFactory.registerSingleton(beanName, initializedBean)
    initializedBean.asInstanceOf[T]
  }

  def newDirectChannel(name: String) = register(name, MessageChannels.direct().get())
}



object ConfigurationContext {
  def apply(springContext: ConfigurableApplicationContext, configRootDir: String) =
    new ConfigurationContext(springContext, new IntegrationFlowBuilder(), configRootDir)

  def apply(springContext: ConfigurableApplicationContext,
            flowBuilder: IntegrationFlowBuilder,
            configRootDir: String) =
    new ConfigurationContext(springContext, flowBuilder, configRootDir)
}