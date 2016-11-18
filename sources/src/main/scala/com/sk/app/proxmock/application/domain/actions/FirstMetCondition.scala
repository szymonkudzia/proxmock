package com.sk.app.proxmock.application.domain.actions

import com.sk.app.proxmock.application.configuration.ConfigurationContext
import com.sk.app.proxmock.application.domain.conditions.Condition

/**
 * Created by Szymon on 22.05.2016.
 */
case class FirstMetCondition(
   conditions: List[CaseAction]
 ) extends Action {

  override def configure(context: ConfigurationContext): Unit = {
    // TODO implement configure in  FirstMetCondition
    throw new NotImplementedError()
  }
}



case class CaseAction(
  condition: Condition,
  action: Action
)
