package com.sk.app.proxmock.application.domain

/**
 * Created by Szymon on 18.05.2016.
 */
case class Config(
   name: String,
   port: String = "8080",
   endpoints: List[Endpoint]
)

