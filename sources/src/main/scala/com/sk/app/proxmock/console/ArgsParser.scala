package com.sk.app.proxmock.console

/**
 * Created by Szymon on 17.05.2016.
 */
private class ArgsParser(
    operations: Map[String, AbstractOperationExecutor],
    args: Array[String],
    errorHandler: Throwable => Unit) {

  try {
    val metaArgs = args.filter(_.startsWith("--"))
    val operArgs = args.filter(!_.startsWith("--"))

    val it = operArgs.iterator

    if (!it.hasNext) {
      // if non args
      operations.get("default").foreach(_.exec("default", it, metaArgs))
    }

    while (it.hasNext) {
      val name = it.next()

      operations.get(name) match {
        case Some(executor) => executor.exec(name, it, metaArgs)
        case None => throw new IllegalArgumentException(s"Unknown command: $name")
      }
    }
  } catch {
    case e: Throwable => errorHandler(e)
  }
}



class ArgsParserBuilder {
  var errorHandler: Throwable => Unit = null
  var operations: Map[String, AbstractOperationExecutor] = Map.empty

  /**
   * @param name program argument describing this operation
   * @param operation operation to be executed when @name was passed in program arguments
   */
  def operation(name: String, operation: Operation) = {
    operations += (name -> OperationExecutor(operation))
    this
  }

  /**
   * @param name program argument describing this operation
   * @param unaryOperation operation to be executed when @name was passed in program arguments
   */
  def unaryOperation(name: String, unaryOperation: UnaryOperation) = {
    operations += (name -> UnaryOperationExecutor(unaryOperation))
    this
  }

  /**
   * @param operation operation to be executed when no arguments were passed
   */
  def defaultOperation(operation: Operation) = {
    operations += ("default" -> OperationExecutor(operation))
    this
  }

  /**
   * @param handler handler for situation when exception was raised during arguments parsing
   */
  def error(handler: (Throwable) => Unit) = {
    errorHandler = handler
    this
  }

  /**
   * Perfomr program arguments parsing
   * @param args program arguments
   */
  def parse(args: Array[String]): Unit = new ArgsParser(operations, args, errorHandler)
}



object ArgsParser {
  /**
   * @param name program argument describing this operation
   * @param operation operation to be executed when @name was passed in program arguments
   */
  def operation(name: String, operation: Operation) = {
    new ArgsParserBuilder().operation(name, operation)
  }


  /**
   * @param name program argument describing this operation
   * @param unaryOperation operation to be executed when @name was passed in program arguments
   */
  def unaryOperation(name: String, unaryOperation: UnaryOperation) =
    new ArgsParserBuilder().unaryOperation(name, unaryOperation)


  /**
   * @param operation operation to be executed when no arguments were passed
   */
  def defaultOperation(operation: Operation) =
    new ArgsParserBuilder().defaultOperation(operation)


  /**
   * @param handler handler for situation when exception was raised during arguments parsing
   */
  def error(handler: Throwable => Unit) =
    new ArgsParserBuilder().error(handler)
}
