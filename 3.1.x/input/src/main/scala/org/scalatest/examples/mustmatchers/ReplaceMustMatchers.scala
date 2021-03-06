/*
rule = RewriteDeprecatedNames
 */
/*
 * Copyright 2001-2013 Artima, Inc.
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
package org.scalatest.examples.mustmatchers

import org.scalatest._

class ReplaceMustMatchers extends FunSpec with MustMatchers {

  describe("A Set") {
    describe("when empty") {
      it("must have size 0") {
        Set.empty.size mustBe 0
      }
    
      it("must produce NoSuchElementException when head is invoked") {
        a [NoSuchElementException] must be thrownBy {
          Set.empty.head
        }
      }
    }
  }
}
