package com.sk.app.proxmock.toolset.serialization

import java.lang.annotation.Annotation

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.deser.{ResolvableDeserializer, BeanDeserializerModifier, ContextualDeserializer}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier

object BetterPolymorphismModule {
  def apply() = {
    val betterPolymorphism = new SimpleModule()
    betterPolymorphism.setDeserializerModifier(new PolymorphicDeserializerModifier())
    betterPolymorphism.setSerializerModifier(new PolymorphicSerializerModifier())
    betterPolymorphism
  }
}

private class PolymorphicDeserializerModifier extends BeanDeserializerModifier {
  override def modifyDeserializer(
    config: DeserializationConfig, beanDesc: BeanDescription, deserializer: JsonDeserializer[_]): JsonDeserializer[_] =
      new PolymorphicDeserializer(deserializer.asInstanceOf[JsonDeserializer[Object]])
}

private class PolymorphicSerializerModifier extends BeanSerializerModifier {
  override def modifySerializer(
     config: SerializationConfig, beanDesc: BeanDescription, serializer: JsonSerializer[_]): JsonSerializer[_] =
       new PolymorphicSerializer(serializer.asInstanceOf[JsonSerializer[Object]])
}



private class PolymorphicDeserializer private (
    defaultDeserializer: JsonDeserializer[Object],
    types: Map[String, Class[_]]) extends JsonDeserializer[Object]
  with ContextualDeserializer with ResolvableDeserializer{

  def this(defaultDeserializer: JsonDeserializer[Object]) = this(defaultDeserializer, Map())

  override def deserialize(p: JsonParser, ctxt: DeserializationContext): Object = {
    types.isEmpty match {
      case true =>
        defaultDeserializer.deserialize(p, ctxt)

      case false =>
        val name = p.nextFieldName()
        p.nextToken()
        val value = p.readValueAs(types(name))
        p.nextToken()

        value.asInstanceOf[Object]
    }
  }

  override def createContextual(ctxt: DeserializationContext, property: BeanProperty): JsonDeserializer[Object] = {
    property match {
      case p: BeanProperty =>
        val actualClass = property.getType.getRawClass
        new PolymorphicDeserializer(defaultDeserializer, JsonSubTypesUtil.asMap(actualClass))

      case null =>
        new PolymorphicDeserializer(defaultDeserializer, Map())
    }
  }

  override def resolve(ctxt: DeserializationContext) =
    if (classOf[ResolvableDeserializer].isAssignableFrom(defaultDeserializer.getClass))
      defaultDeserializer.asInstanceOf[ResolvableDeserializer].resolve(ctxt)
}


private class PolymorphicSerializer(defaultSerializer: JsonSerializer[Object]) extends JsonSerializer[Object] {

  override def serialize(value: Object, gen: JsonGenerator, serializers: SerializerProvider): Unit = {
    val nameOption = JsonSubTypesUtil.asMapInverted(value.getClass).get(value.getClass)

    nameOption match {
      case Some(name) =>
        gen.writeStartObject()
        gen.writeFieldName(name)
        defaultSerializer.serialize(value, gen, serializers)
        gen.writeEndObject()

      case None =>
        defaultSerializer.serialize(value, gen, serializers)
    }
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

