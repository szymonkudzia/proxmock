package com.sk.app.proxmock.toolset.serialization.betterpolymorphism

import java.lang.annotation.Annotation

import com.fasterxml.jackson.annotation.JsonSubTypes

/**
 * Created by Szymon on 22.05.2016.
 */
private[betterpolymorphism] object JsonSubTypesUtil {
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
