package com.sk.app.proxmock.mock.domain

import com.sk.app.proxmock.mock.domain.actions.Action

/**
 * Created by Szymon on 20.05.2016.
 */
case class Endpoint(
  path: String,
  action: Action
)

