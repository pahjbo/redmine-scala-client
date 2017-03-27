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

package by.exonit.redmine.client.serialization

import by.exonit.redmine.client._
import org.json4s._

import scala.collection.immutable._

object ModuleSerializers {
  lazy val all: Seq[Serializer[_]] = Seq(
    moduleIdSerializer,
    moduleLinkSerializer)

  def deserializeModuleId(implicit formats: Formats): PartialFunction[JValue, ModuleId] = {
    case JInt(id) => ModuleId(id)
  }

  def serializeModuleId(implicit formats: Formats): PartialFunction[Any, JValue] = {
    case ModuleId(id) => JInt(id)
  }

  object moduleIdSerializer extends CustomSerializer[ModuleId](
    formats => deserializeModuleId(formats) -> serializeModuleId(formats))

  def deserializeModuleLink(implicit formats: Formats): PartialFunction[JValue, ModuleLink] = {
    case j: JObject =>
      ModuleLink(
        (j \ "id").extract[BigInt],
        (j \ "name").extract[String])
  }

  object moduleLinkSerializer extends CustomSerializer[ModuleLink](
    formats => deserializeModuleLink(formats) -> PartialFunction.empty)

}
