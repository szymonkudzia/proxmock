package com.sk.app.proxmock.application.domain.actions

import com.sk.app.proxmock.application.domain.conditions.Condition

/**
 * Created by Szymon on 22.05.2016.
 */
case class FirstMetCondition(
   conditions: List[ConditionalAction]
 ) extends Action



case class ConditionalAction(
  condition: Condition,
  action: Action
)
