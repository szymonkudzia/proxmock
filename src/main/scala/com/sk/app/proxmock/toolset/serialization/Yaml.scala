package com.sk.app.proxmock.toolset.serialization

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.scala.DefaultScalaModule

/**
 * Created by Szymon on 18.05.2016.
 */
object Yaml {
  private val mapper = new ObjectMapper(new YAMLFactory())
  mapper.registerModule(DefaultScalaModule)
  mapper.enable(SerializationFeature.INDENT_OUTPUT)
  mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
  mapper.disable(JsonGenerator.Feature.QUOTE_FIELD_NAMES)
  mapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)


  mapper.registerModule(BetterPolymorphismModule())

  def parse[T](yaml: String, clazz: Class[T]): T =
    mapper.readValue(yaml, clazz)

  def serialize[T](o: T): String =
    mapper.writeValueAsString(o)
}
