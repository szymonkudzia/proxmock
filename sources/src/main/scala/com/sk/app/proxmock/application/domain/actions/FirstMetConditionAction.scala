package com.sk.app.proxmock.application.domain.actions

import com.fasterxml.jackson.annotation.JsonCreator
import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.application.domain.conditions.Condition
import org.springframework.integration.dsl.support.{Consumer, Function}
import org.springframework.integration.dsl.{IntegrationFlow, IntegrationFlowDefinition, RouterSpec}
import org.springframework.integration.router.MethodInvokingRouter
import org.springframework.messaging.Message

/**
  * Created by Szymon on 22.05.2016.
  */
case class FirstMetConditionAction(cases: Array[CaseAction]) extends Action {

  override def configure(context: ConfigurationContext): Unit = {
    context.flowBuilder.route(conditionEvaluation(context), subflowsConfigurer(context))
  }

  def conditionEvaluation(context: ConfigurationContext) =
    new Function[Message[Object], String] {
      override def apply(message: Message[Object]): String =
        "case_" + cases.indexWhere(_.condition.test(message, context))
    }


  def subflowsConfigurer(context: ConfigurationContext) =
    new Consumer[RouterSpec[MethodInvokingRouter]] {
      override def accept(mapping: RouterSpec[MethodInvokingRouter]): Unit = {
        cases.zipWithIndex.foreach { case (c, index) =>
          mapping.subFlowMapping(s"case_$index", configureAction(c.action, context))
        }
      }
    }


  def configureAction(action: Action, context: ConfigurationContext) =
    new IntegrationFlow {
      override def configure(flow: IntegrationFlowDefinition[_]): Unit = {
        val flowBuilder = context.flowBuilder
        context.flowBuilder = flow
        action.configure(context)
        context.flowBuilder = flowBuilder
      }
    }
}

object FirstMetConditionAction {
  @JsonCreator
  def create(cases: Array[CaseAction]) = FirstMetConditionAction(cases)
}

case class CaseAction(condition: Condition, action: Action)
