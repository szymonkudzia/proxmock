package com.sk.app.proxmock.application.domain.conditions

/**
 * Created by Szymon on 22.05.2016.
 */
case class HeaderEquals(
   headerName: String,
   headerValue: String
 ) extends Condition
