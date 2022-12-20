package org.test
package nonceCalc

import nonceCalc.HashSHA256.*

import cats.effect.*
import cats.implicits.*

object Worker {

  private val batchSize = 1000000

  private val terminalNonce = 1000000000

  case class State[F[_]](endNonce: Int, initHash: Array[Byte], resultRef: Ref[F, Option[(Int, Array[Byte])]], nextBatchStart: Ref[F, Int])

  def apply[F[_]](initHash: Array[Byte], nextBatchStart: Ref[F, Int], resultRef: Ref[F, Option[(Int, Array[Byte])]])(implicit
      F: Sync[F]
  ): F[Unit] =
    for {
      res <- resultRef.get
      _ <-
        res match {
          case Some(value) =>
            F.unit
          case None =>
            for {
              currentNonce <- nextBatchStart.modify(x => (x + batchSize, x))
              _ <- F.delay(println(s"Restarted with ${currentNonce} ${Thread.currentThread().getId()}"))
              _ <- run(
                currentNonce,
                State(endNonce = currentNonce + batchSize, initHash = initHash, resultRef = resultRef, nextBatchStart = nextBatchStart)
              )
            } yield ()
        }
    } yield ()

  def run[F[_]](currentNonce: Int, s: State[F])(implicit F: Sync[F]): F[Unit] =
    if (currentNonce >= terminalNonce) {
      F.unit
    } else if (currentNonce < s.endNonce) {
      val withNonce = hashWithNonce(s.initHash, currentNonce)
      val isFound = test(withNonce, 5)
      if (isFound) {
        s.resultRef.set(Some(currentNonce, withNonce))
      } else {
        run[F](currentNonce + 1, s)
      }
    } else {
      apply(s.initHash, s.nextBatchStart, s.resultRef)
    }
}

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO.unit
      initHash = hash("123123123123".getBytes("UTF-8"))
      resultRef <- Ref[IO].of(Option.empty[(Int, Array[Byte])])
      nonceRef <- Ref[IO].of(0)
      _ <- List.fill(4)(Worker[IO](initHash, nonceRef, resultRef)).parSequence.void
      result <- resultRef.get
      _ <- IO.println {
        result match {
          case Some((n, h)) =>
            (n, byteToHex(h))
          case x =>
            x
        }
      }
    } yield ExitCode.Success
}
