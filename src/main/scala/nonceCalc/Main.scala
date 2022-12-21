package org.test
package nonceCalc

import nonceCalc.HashSHA256.*

import cats.effect.*
import cats.implicits.*
import cats.Applicative
import cats.instances.byte
import scala.concurrent.duration.Deadline
import java.util.concurrent.TimeUnit

object Worker {

  private val batchSize = 1000000

  private val terminalNonce = 1000000000

  private val zeroBytesCount = 1

  case class State[F[_]](endNonce: Int, initHash: Array[Byte], resultRef: Deferred[F, (Int, Array[Byte])], nextBatchStart: Ref[F, Int])

  def apply[F[_]](initHash: Array[Byte], nextBatchStart: Ref[F, Int], resultRef: Deferred[F, (Int, Array[Byte])])(implicit
      F: Sync[F]
  ): F[Unit] =
    for {
      res <- resultRef.tryGet
      _ <- res
        .map(_ => F.unit)
        .getOrElse {
          for {
            currentNonce <- nextBatchStart.modify(x => (x + batchSize, x))
            _ <- F.delay(println(s"Restarted with ${currentNonce} ${Thread.currentThread().getId()}"))
            _ <- run(currentNonce, State(currentNonce + batchSize, initHash, resultRef, nextBatchStart))
          } yield ()
        }
    } yield ()

  def run[F[_]](currentNonce: Int, s: State[F])(implicit F: Sync[F]): F[Unit] =
    if (currentNonce >= terminalNonce) {
      F.delay(println(s"Termitated at ${currentNonce} ${Thread.currentThread().getId()}"))
    } else if (currentNonce < s.endNonce) {
      val withNonce = hashWithNonce(s.initHash, currentNonce)
      val isFound = test(withNonce, zeroBytesCount)
      if (isFound) {
        F.delay(println(s"Found at ${currentNonce} ${Thread.currentThread().getId()}")) >> s.resultRef.complete(currentNonce, withNonce) >>
          F.unit
      } else {
        run(currentNonce + 1, s)
      }
    } else {
      apply(s.initHash, s.nextBatchStart, s.resultRef)
    }
}

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    for {
      initHash <- IO(hash("12312312311231231232123".getBytes("UTF-8")))
      resultRef <- IO.deferred[(Int, Array[Byte])]
      nonceRef <- Ref[IO].of(0)
      _ <- benchmark {
        List.fill(4)(Worker[IO](initHash, nonceRef, resultRef)).parSequence.void
      }
      result <- resultRef.get
      _ <- IO.println {
        result match {
          case (n, h) =>
            (n, byteToHex(h))
        }
      }

    } yield ExitCode.Success
}

def benchmark[A](a: => IO[A]): IO[A] =
  for {
    startTime <- IO(Deadline.now)
    result <- a
    endTime <- IO(Deadline.now)
    _ <- IO.println(s"${(endTime - startTime).toUnit(TimeUnit.SECONDS)} seconds")
  } yield result
