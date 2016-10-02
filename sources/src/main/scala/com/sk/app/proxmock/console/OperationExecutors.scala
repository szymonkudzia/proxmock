package com.sk.app.proxmock.console


private[console] abstract class AbstractOperationExecutor() {
  def exec(name: String, argsIterator: Iterator[String], metaArgs: Array[String])
}



private[console] case class OperationExecutor(operation: Operation) extends AbstractOperationExecutor{
  override def exec(name: String, argsIterator: Iterator[String], metaArgs: Array[String]) =
    operation(metaArgs)
}

private[console] case class UnaryOperationExecutor(operation: UnaryOperation) extends AbstractOperationExecutor{

  override def exec(name: String, argsIterator: Iterator[String], metaArgs: Array[String]) = {
    assert(argsIterator.hasNext, s"Two few arguments were provided for $name operation!")

    operation(argsIterator.next(), metaArgs)
  }
}