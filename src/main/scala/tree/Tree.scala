package org.test
package tree

import tree.Tree._
import util.Implicits._

import cats.implicits._
import cats.{Alternative, Monad}

import scala.annotation.tailrec

sealed trait Tree[+A] {

  def +[S >: A](newValue: S)(implicit ordering: Ordering[S]): Tree[S] = this match {
    case Leaf(value) => ordering.compare(newValue, value) match {
      case x if x < 0 => Branch(value, Leaf(newValue), NilLeaf)
      case x if x == 0 => this
      case x if x > 0 => Branch(value, NilLeaf, Leaf(newValue))
    }
    case Branch(value, left, right) => ordering.compare(newValue, value) match {
      case x if x < 0 => Branch(value, left + newValue, right)
      case x if x == 0 => this
      case x if x > 0 => Branch(value, left, right + newValue)
    }
    case NilLeaf => Leaf(newValue)
  }

  def depth: Int = this match {
    case Branch(_, left, right) => math.max(left.depth + 1, right.depth + 1)
    case Leaf(_) => 1
    case NilLeaf => 0
  }

  @tailrec
  final def max: Option[A] = this match {
    case Branch(branchValue, _, right) => right match {
      case Branch(_, _, _) => right.max
      case Leaf(value) => Option(value)
      case NilLeaf => Option(branchValue)
    }
    case Leaf(value) => Option(value)
    case NilLeaf => None
  }

  @tailrec
  final def min: Option[A] =
    this match {
      case Branch(branchValue, left, _) => left match {
        case Branch(_, _, _) => left.min
        case Leaf(value) => Option(value)
        case NilLeaf => Option(branchValue)
      }
      case Leaf(value) => Option(value)
      case NilLeaf => None
    }
}

object Tree {
  def apply[A](value: A)(implicit ordering: Ordering[A]): Tree[A] = {
    Leaf(value)
  }

  def apply[A](values: A*)(implicit ordering: Ordering[A]): Tree[A] = {
    def loop(tree: Seq[A]): Tree[A] = {
      tree match {
        case x +: rest => loop(rest) + x
        case Nil => NilLeaf
      }
    }

    loop(values.reverse)
  }

  final case class Branch[A](value: A, left: Tree[A], right: Tree[A]) extends Tree[A]

  final case class Leaf[A](value: A) extends Tree[A]

  case object NilLeaf extends Tree[Nothing]

  implicit final class FoldOrder[A](private val tree: Tree[A]) extends AnyVal {
    private def fold[S >: A, F[_]](f: (F[S], F[S], F[S]) => F[S])(implicit alt: Alternative[F]): F[S] = {
      def loop(tree: Tree[A]): F[S] = {
        tree match {
          case Branch(value, left, right) => f(loop(left), loop(right), alt.pure(value))
          case Leaf(value) => alt.pure(value)
          case NilLeaf => alt.empty
        }
      }

      loop(tree)
    }

    def foldPostOrder[F[_]](implicit alt: Alternative[F]): F[A] = fold[A, F]((l, r, v) => l <+> r <+> v)

    def foldPreorder[F[_]](implicit alt: Alternative[F]): F[A] = fold[A, F]((l, r, v) => v <+> l <+> r)

    def foldInorder[F[_]](implicit alt: Alternative[F]): F[A] = fold[A, F]((l, r, v) => l <+> v <+> r)
  }
}

object Application {

  def main(args: Array[String]): Unit = {
    Tree(1, 2, 3)
      .print
      .foldInorder[List]
      .print
    IndexedSeq(1, 2, 3)
  }
}

