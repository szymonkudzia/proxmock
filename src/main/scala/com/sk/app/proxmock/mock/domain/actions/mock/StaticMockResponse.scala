package com.sk.app.proxmock.mock.domain.actions.mock

import com.sk.app.proxmock.mock.domain.actions.Action

/**
 * Created by Szymon on 22.05.2016.
 */
case class StaticMockResponse(
   headers: Map[String, String],
   bodyContent: String,
   bodyPath: String
 ) extends Action
