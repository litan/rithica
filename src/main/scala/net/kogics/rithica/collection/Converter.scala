package net.kogics.rithica.collection
import scala.jdk.CollectionConverters._

object Converter {
  implicit def jListAsScala[T](jl: java.util.List[T]) = jl.asScala
  implicit def seqAsJava[T](seq: collection.Seq[T]) = seq.asJava
}
