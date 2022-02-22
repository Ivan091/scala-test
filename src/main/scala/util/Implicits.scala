package org.test
package util

object Implicits {
  implicit class Printer[A] (val x: A) extends AnyVal {
    def print: A = {
      println(x)
      x
    }
  }
}
