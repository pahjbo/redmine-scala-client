/*
 * Copyright 2017 Exon IT
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

package by.exonit.redmine.client.playws.fixtures

import com.github.restdriver.clientdriver.{ClientDriver, ClientDriverFactory}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Suite}

import scala.util.Try

trait ClientDriverFixture extends BeforeAndAfterEach with BeforeAndAfterAll {
  this: Suite =>

  val clientDriver: ClientDriver = new ClientDriverFactory().createClientDriver()

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    clientDriver.reset()
  }

  override protected def afterAll(): Unit = {
    clientDriver.shutdownQuietly()
    Try(super.afterAll())
  }
}
