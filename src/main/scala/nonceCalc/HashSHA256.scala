package org.test
package nonceCalc

import common.implicits.*

import cats.implicits.*

import java.util.HexFormat

object HashSHA256 {

  def hash(in: Array[Byte]): Array[Byte] = java.security.MessageDigest.getInstance("SHA-256").digest(in)

  def hashWithNonce(prev: Array[Byte], nonce: Int): Array[Byte] = hash(prev |+| intToBytes(nonce))

  def test(in: Array[Byte], zerosCount: Int): Boolean = {
    var i = 0
    while i < zerosCount do {
      if (in(i) != 0) {
        return false
      }
      i += 1
    }
    true
  }

  def byteToHex(in: Array[Byte]): String = in.map("%02X".format(_)).mkString

  private def intToBytes(nonce: Int): Array[Byte] = {
    val bb = java.nio.ByteBuffer.allocate(4)
    bb.putInt(nonce)
    bb.array()
  }
}
