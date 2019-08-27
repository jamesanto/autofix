/*
 * Copyright 2001-2015 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalatest.examples.fixtureasyncfeaturespeclike

import org.scalatest._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import org.scalatest.featurespec

// Defining actor messages
sealed abstract class StringOp
case object Clear extends StringOp
case class Append(value: String) extends StringOp
case object GetValue

class StringActor { // Simulating an actor
  private final val sb = new StringBuilder
  def !(op: StringOp): Unit =
    synchronized {
      op match {
        case Append(value) => sb.append(value)
        case Clear => sb.clear()
      }
    }
  def ?(get: GetValue.type)(implicit c: ExecutionContext): Future[String] =
    Future {
      synchronized { sb.toString }
    }
}

class ReplaceFixtureAsyncFeatureSpecLike extends featurespec.FixtureAsyncFeatureSpecLike {

  type FixtureParam = StringActor

  def withFixture(test: OneArgAsyncTest): FutureOutcome = {

    val actor = new StringActor
    complete {
      actor ! Append("ScalaTest is designed to ") // set up the fixture
      withFixture(test.toNoArgAsyncTest(actor))
    } lastly {
      actor ! Clear // ensure the fixture will be cleaned up
    }
  }

  Feature("Simplicity") {
    Scenario("User needs to read test code written by others") { actor =>
      actor ! Append("encourage clear code!")
      val futureString = actor ? GetValue
      futureString map { s =>
        assert(s === "ScalaTest is designed to encourage clear code!")
      }
    }

    Scenario("User needs to understand what the tests are doing") { actor =>
      actor ! Append("be easy to reason about!")
      val futureString = actor ? GetValue
      futureString map { s =>
        assert(s === "ScalaTest is designed to be easy to reason about!")
      }
    }
  }

  def testScenariosFor(): Unit = {
    Scenario("scenario 1") { f =>
      succeed
    }

    Scenario("scenario 2") { f =>
      succeed
    }
  }

  ScenariosFor(testScenariosFor())
}
