package org.test
package automata

import cats.effect.unsafe.implicits.global
import cats.effect.{ExitCode, IO, IOApp}

object Application extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    while (true) {
      {
        for {
          x <- IO.readLine
          res <- IO(parse(x))
          _ <- IO.println(res)
        } yield ()
      }.unsafeRunSync()
    }
    IO(ExitCode.Success)
  }

  def parse(s: String): Boolean = {
    var i = 0
    val vector = s.toCharArray

    def m(ch: Char) = {
      if (i >= vector.length){
        false
      } else {
        val res = ch == vector(i)
        i += 1
        res
      }
    }

    def exp: Boolean = {
      val s = i;
      {
        i = s
        term && m('+') && exp
      } || {
        i = s
        term
      }
    }

    def term: Boolean = {
      val s = i;
      {
        i = s
        m('(') && exp && m(')')
      } || {
        i = s
        int && m('*') && term
      } || {
        i = s
        int
      }
    }

    def int: Boolean = {
      val s = i;
      {
        i = s
        m('1')
      } || {
        i = s
        m('2')
      } || {
        i = s
        m('3')
      } || {
        i = s
        m('4')
      } || {
        i = s
        m('5')
      } || {
        i = s
        m('6')
      } || {
        i = s
        m('7')
      } || {
        i = s
        m('8')
      } || {
        i = s
        m('9')
      } || {
        i = s
        m('0')
      }
    }

    exp
  }
}
