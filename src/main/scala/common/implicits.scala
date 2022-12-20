package org.test
package common

import cats.Monoid

import scala.reflect.ClassTag

object implicits {
  implicit def arrayMonoid[A: ClassTag]: Monoid[Array[A]] =
    new Monoid[Array[A]] {
      def empty: Array[A] = Array()

      def combine(x: Array[A], y: Array[A]): Array[A] = x.appendedAll(y)
    }
}
