package com.sk.app.proxmock.application.domain.conditions

import org.springframework.messaging.Message

import scala.util.Random

/**
 * Created by Szymon on 22.05.2016.
 */
case class RandomCondition() extends Condition {
  val random = new Random()

  override def test(message: Message[Object]): Boolean = random.nextBoolean()
}
