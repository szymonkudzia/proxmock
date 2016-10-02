package com.sk.app.proxmock

/**
 * Created by Szymon on 17.05.2016.
 */
package object console {
  type UnaryOperation = (String, Array[String]) => Unit
  type Operation = (Array[String]) => Unit
}
