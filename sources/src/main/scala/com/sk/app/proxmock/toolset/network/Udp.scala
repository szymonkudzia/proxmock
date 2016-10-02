package com.sk.app.proxmock.toolset.network

import java.net.{DatagramPacket, InetAddress, MulticastSocket, SocketException}
import java.nio.charset.StandardCharsets

import rx.lang.scala.schedulers.IOScheduler
import rx.lang.scala.{Observable, Observer, Subscription}

/**
 * Created by Szymon on 15.05.2016.
 */
class Udp(port: Int = 61212) {
  private val address: InetAddress = InetAddress.getByName("224.0.0.0")

  private val socket = new MulticastSocket(port)
  socket.setReuseAddress(true)
  socket.setBroadcast(true)
  socket.joinGroup(address)

  private var closedByUser = false

  private val observable = Observable[String](observer => {
    receive(observer)
    Subscription {}
  }).subscribeOn(IOScheduler())

  def asObservable() = observable


  def listen(onNext: String => Unit) = {
    observable.subscribe(onNext)
    this
  }

  def send(message: String) = {
    val bytes = message.getBytes(StandardCharsets.UTF_8)
    socket.send(new DatagramPacket(bytes, bytes.length, address, port))
    this
  }

  def close() = {
    closedByUser = true
    socket.leaveGroup(address)
    socket.close()
  }

  private def receive(observer: Observer[String]) =
    try {
      val buffer = Array.ofDim[Byte](2028)
      val packet = new DatagramPacket(buffer, buffer.length)

      while (true) {
        socket.receive(packet)
        observer onNext new String(buffer, 0, packet.getLength)
      }
      observer.onCompleted()

    } catch {
      case e: SocketException =>
        closedByUser match {
          case true => observer.onCompleted()
          case _ => observer.onError(e)
        }
      case e: Exception => observer.onError(e)
    }
}
