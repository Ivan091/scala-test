package org.test
package nonceCalc

object Main {
  def main(args: Array[String]): Unit = {
    val res = HashSHA256.byteToHex{
      "123".getBytes.appendedAll {
        val bb = java.nio.ByteBuffer.allocate(4)
        bb.putInt(2083236893)
        bb.array()
      }
    }
    println(HashSHA256.byteToHex("123".getBytes))
    println(res)
  }
}
