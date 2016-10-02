package com.sk.app.proxmock.toolset.serialization.betterpolymorphism

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.deser.{ContextualDeserializer, ResolvableDeserializer}
import com.fasterxml.jackson.databind.{BeanProperty, DeserializationContext, JsonDeserializer}

/**
 * Created by Szymon on 22.05.2016.
 */
private[betterpolymorphism] class PolymorphicDeserializer private (
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
