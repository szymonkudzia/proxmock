package com.sk.app.proxmock.application.configuration

import com.sk.app.proxmock.application.domain.Config

/**
 * Created by Szymon on 19.05.2016.
 */
object MockArguments {
  def apply(config: Config, args: Array[String]): Array[String] = {
    var result = Map[String, String]()

    result += "--server.port" -> config.port

    result ++= argumentsToMap(args)
    argumentsMapAsArray(result)
  }

  def argumentsMapAsArray(result: Map[String, String]): Array[String] = {
    result.map { case (key, value) => s"$key=$value" }.toArray
  }

  def argumentsToMap(args: Array[String]): Map[String, String] = {
    args
      .map(_.split("="))
      .map(keyValue => keyValue(0) -> keyValue(1))
      .toMap
  }
}
