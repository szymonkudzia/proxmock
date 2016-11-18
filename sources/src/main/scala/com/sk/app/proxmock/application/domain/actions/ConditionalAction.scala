package com.sk.app.proxmock.application.domain.actions

import java.util.Objects._

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.application.domain.conditions.Condition
import org.springframework.integration.dsl.{IntegrationFlow, IntegrationFlowDefinition, RouterSpec}
import org.springframework.integration.dsl.support.Consumer
import org.springframework.integration.router.MethodInvokingRouter
import org.springframework.messaging.Message
import org.springframework.integration.dsl.support.Function

/**
  * Created by szymo on 18/11/2016.
  */
case class ConditionalAction (
   condition: Condition,
   ifTrue: Action,
   ifFalse: Action
 ) extends Action {
    requireNonNull(condition, "condition of conditional action cannot be null")
    requireNonNull(ifTrue, "ifTrue of conditional action cannot be null")
    requireNonNull(ifFalse, "ifFalse of conditional action cannot be null")

  override def configure(context: ConfigurationContext): Unit = {
    context.flowBuilder.route(new ConditionEvaluation(), new SubflowsConfigurer(context))
  }

  private class ConditionEvaluation extends Function[Message[Object], Boolean] {
    override def apply(message: Message[Object]): Boolean = condition.test(message)
  }

  private class SubflowsConfigurer(context: ConfigurationContext) extends Consumer[RouterSpec[MethodInvokingRouter]] {
    override def accept(mapping: RouterSpec[MethodInvokingRouter]): Unit = {
      mapping
        .subFlowMapping("true", new ConfigureAction(ifTrue, context))
        .subFlowMapping("false", new ConfigureAction(ifFalse, context))
    }
  }


  private class ConfigureAction(action: Action, context: ConfigurationContext) extends IntegrationFlow {
    override def configure(flow: IntegrationFlowDefinition[_]): Unit = {
      val flowBuilder = context.flowBuilder
      context.flowBuilder = flow
      action.configure(context)
      context.flowBuilder = flowBuilder
    }
  }
}
