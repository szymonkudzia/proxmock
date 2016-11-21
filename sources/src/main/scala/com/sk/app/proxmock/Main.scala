package com.sk.app.proxmock

import java.util.concurrent.TimeUnit

import com.sk.app.proxmock.application.ProxmockApplication
import com.sk.app.proxmock.console.ArgsParser
import org.slf4j.LoggerFactory

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.paint.Color._


/**
 * Created by Szymon on 15.05.2016.
 */
object Main extends JFXApp {
  stage = new PrimaryStage {
    title = "ScalaFX Hello World"
    scene = new Scene {
      fill = Black
      //      content = new HBox {
      //        padding = Insets(20)
      //        children = Seq(
      //          new Text {
      //            text = "Hello "
      //            style = "-fx-font-size: 48pt"
      //            fill = new LinearGradient(
      //              endX = 0,
      //              stops = Stops(PaleGreen, SeaGreen))
      //          },
      //          new Text {
      //            text = "World!!!"
      //            style = "-fx-font-size: 48pt"
      //            fill = new LinearGradient(
      //              endX = 0,
      //              stops = Stops(Cyan, DodgerBlue)
      //            )
      //            effect = new DropShadow {
      //              color = DodgerBlue
      //              radius = 25
      //              spread = 0.25
      //            }
      //          }
      //        )
      //      }
    }
  }

  def showHelp() = {
    println(
      """
        |list of available commands:
        |  list          - list all running instances
        |  stop n        - stop instance with name: n
        |  help          - displays this information
        |  run filepath  - runs proxymock in background with configuration fetched from file under filepath
        |                  This command accepts also additional parameters (--name) used by spring boot which
        |                  can be used to change proxmock behaviour. i.e.:
        |                   proxmock run /file.yaml --server.port=9090
        |                  See spring boot documentation for more information about available properties
        |
        |i.e.:
        |proxmock.jar list
        |- above command will list all running instances
        |
        |proxmoc.jar stop baka
        |- above command will stop instance with name "baka"
      """.stripMargin)
  }

  def listRemote() = {
    ProxmockApplication.listRemote()
    TimeUnit.SECONDS.sleep(1)
  }

  def closeRemote(name: String) = {
    ProxmockApplication.closeRemote(name)
    TimeUnit.SECONDS.sleep(1)
  }

  def runMock(filePath: String, metaArgs: Array[String]) = {
    ProxmockApplication.run(filePath, metaArgs)
  }

  override def main(args: Array[String]) = {
    ArgsParser
      .operation("help", _ => showHelp())
      .operation("list", _ => listRemote())
      .unaryOperation("stop", (name, _) => closeRemote(name))
      .unaryOperation("run", (filePath, metaArgs) => runMock(filePath, metaArgs))
      .defaultOperation(_ => super.main(args))
      .error(exception => {
        exception.printStackTrace(System.out)
        showHelp()
      })
      .parse(args)
  }
}
