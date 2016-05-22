package com.sk.app.proxmock.application.domain

import com.sk.app.proxmock.application.domain.actions.Action

/**
 * Created by Szymon on 20.05.2016.
 */
case class Endpoint(
  path: String,
  action: Action
)

