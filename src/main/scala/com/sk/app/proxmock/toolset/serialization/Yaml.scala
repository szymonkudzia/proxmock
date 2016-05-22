package com.sk.app.proxmock.toolset.serialization

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.sk.app.proxmock.toolset.serialization.betterpolymorphism.BetterPolymorphismModule

/**
 * Created by Szymon on 18.05.2016.
 */
object Yaml {
  private val mapper = new ObjectMapper(new YAMLFactory())
  mapper.registerModule(DefaultScalaModule)
  mapper.registerModule(BetterPolymorphismModule())

  mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
  mapper.enable(JsonParser.Feature.ALLOW_YAML_COMMENTS)
  mapper.enable(SerializationFeature.INDENT_OUTPUT)

  def parse[T](yaml: String, clazz: Class[T]): T =
    mapper.readValue(yaml, clazz)

  def serialize[T](o: T): String =
    mapper.writeValueAsString(o)
}
