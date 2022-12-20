package org.test
package nonceCalc

import java.nio.ByteBuffer

object HashSHA256 {

  val hasher = java.security.MessageDigest.getInstance("SHA-256")

  def hash(in: Array[Byte]): Array[Byte] = {
    hasher.digest(in)
  }

  def test(prev: Array[Byte], nonce: Int, test: Array[Byte] => Boolean) = {
    test {
      prev.appendedAll{
        val bb = java.nio.ByteBuffer.allocate(4)
        bb.putInt(nonce)
        bb.array()
      }
    }
  }

  def byteToHex(in: Array[Byte]): String = {
    in.map("%02X".format(_)).mkString
  }
}
