package com.sk.app.proxmock

import java.io.File
import java.util.concurrent.TimeUnit

import com.sk.app.proxmock.console.ArgsParser
import com.sk.app.proxmock.application.MockApplication
import com.sk.app.proxmock.application.domain._
import com.sk.app.proxmock.application.domain.actions.mock.StaticMockResponse
import com.sk.app.proxmock.application.domain.actions.{ConditionalAction, FirstMetCondition}
import com.sk.app.proxmock.application.domain.conditions.HeaderEquals
import com.sk.app.proxmock.toolset.serialization.Yaml
import org.apache.commons.io.FileUtils

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
    MockApplication.listRemote()
    TimeUnit.SECONDS.sleep(1)
  }

  def closeRemote(name: String) = {
    MockApplication.closeRemote(name)
    TimeUnit.SECONDS.sleep(1)
  }

  def runMock(filePath: String, metaArgs: Array[String]) = {
    MockApplication.run(filePath, metaArgs)
  }

  override def main(args: Array[String]) = {
    var firstResponse = StaticMockResponse(Map("corrId" -> "1334-dsf23-345"), "{id:3424}", null)
    var secondResponse = StaticMockResponse(Map("corrId" -> "1334-dsf23-345"), null, "c:/response.json")

    val first = ConditionalAction(HeaderEquals("bigHead", "really big"), firstResponse)
    val second = ConditionalAction(HeaderEquals("smallHead", "really small"), secondResponse)

    val pickFirst = FirstMetCondition(List(first, second))
    val endpoints = List(Endpoint("/some/path", pickFirst))

    val content = Yaml.serialize(Config("cool name", "9090", endpoints))
    FileUtils.write(new File("c:\\Users\\Szymon\\Desktop\\test.yaml"), content)

    val parsed = Yaml.parse(content, classOf[Config])
    println(parsed)

    ArgsParser
      .operation("help", _ => showHelp())
      .operation("list", _ => listRemote())
      .unaryOperation("stop", (name, _) => closeRemote(name))
      .unaryOperation("run", (filePath, metaArgs) => runMock(filePath, metaArgs))
      .defaultOperation(_ => super.main(args))
      .error(exception => {
        println(exception.getMessage)
        showHelp()
      })
      .parse(args)
  }
}
