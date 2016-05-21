package com.sk.app.proxmock.toolset.serialization

import java.lang.annotation.Annotation

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule

object BetterPolymorphismModule {
  def apply() = {
    val betterPolymorphism = new SimpleModule()
    betterPolymorphism.addDeserializer(classOf[Wrapper[_]], new PolymorphicDeserializer())
    betterPolymorphism.addSerializer(classOf[Wrapper[_]], new PolymorphicSerializer())
    betterPolymorphism
  }
}


case class Wrapper[T](value: T) {
  def valueClass() = value.getClass
  def apply() = value
}


private class PolymorphicDeserializer private (types: Map[String, Class[_]]) extends JsonDeserializer[Wrapper[_]] with ContextualDeserializer{
  def this() = this(Map())

  override def deserialize(p: JsonParser, ctxt: DeserializationContext): Wrapper[_] = {
    val name = p.nextFieldName()
    p.nextToken()
    val value = p.readValueAs(types(name))
    p.nextToken()

    Wrapper(value)
  }

  override def createContextual(ctxt: DeserializationContext, property: BeanProperty): JsonDeserializer[_] = {
    val actualClass = property.getType.getBindings.getBoundType(0).getRawClass

    new PolymorphicDeserializer(JsonSubTypesUtil.asMap(actualClass))
  }
}


private class PolymorphicSerializer extends JsonSerializer[Wrapper[_]] {
  override def serialize(value: Wrapper[_], gen: JsonGenerator, serializers: SerializerProvider): Unit = {
    val name = JsonSubTypesUtil.asMapInverted(value.valueClass())(value.valueClass())

    gen.writeStartObject()
    gen.writeObjectField(name, value())
    gen.writeEndObject()
  }
}

private object JsonSubTypesUtil {
  def asMap(clazz: Class[_]): Map[String, Class[_]] = {
    findAnnotation(clazz, classOf[JsonSubTypes]) match {
      case Some(subTypes) => subTypes
        .value()
        .map(typeInfo => typeInfo.name() -> typeInfo.value())
        .toMap

      case None => Map()
    }
  }

  def asMapInverted(clazz: Class[_]): Map[Class[_], String] = {
    findAnnotation(clazz, classOf[JsonSubTypes]) match {
      case Some(subTypes) => subTypes
        .value()
        .map(typeInfo => typeInfo.value() -> typeInfo.name())
        .toMap

      case None => Map()
    }
  }

  private def findAnnotation[T <: Annotation](clazz: Class[_], annoationClass: Class[T]): Option[T] = {
    var found = clazz

    while (found.getSuperclass != null) {
      val annotation = found.getAnnotation(annoationClass)
      if (annotation != null) return Option(annotation)

      found = found.getSuperclass
    }

    None
  }
}

