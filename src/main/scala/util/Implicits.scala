package org.test
package util

extension[A](x: A) {
  def print: A = {
    println(x)
    x
  }
}
