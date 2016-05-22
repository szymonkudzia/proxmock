package com.sk.app.proxmock.toolset.serialization.betterpolymorphism

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{SerializerProvider, JsonSerializer}

/**
 * Created by Szymon on 22.05.2016.
 */
private[betterpolymorphism] class PolymorphicSerializer(defaultSerializer: JsonSerializer[Object]) extends JsonSerializer[Object] {

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
