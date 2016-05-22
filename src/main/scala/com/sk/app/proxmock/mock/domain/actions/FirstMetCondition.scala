package com.sk.app.proxmock.mock.domain.actions

import com.sk.app.proxmock.mock.domain.conditions.Condition

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
