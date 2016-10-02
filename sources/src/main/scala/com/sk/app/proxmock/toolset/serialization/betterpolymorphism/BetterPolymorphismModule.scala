package com.sk.app.proxmock.toolset.serialization.betterpolymorphism

import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
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










