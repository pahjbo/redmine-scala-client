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

import by.exonit.redmine.client.{Activity, ActivityId, ActivityLink, CustomField}
import org.json4s._

import scala.collection.immutable._

object ActivitySerializers {
  lazy val all: Seq[Serializer[_]] = Seq(activityIdSerializer, activityLinkSerializer, activitySerializer)

  def deserializeActivityId(implicit formats: Formats): PartialFunction[JValue, ActivityId] = {
    case JInt(id) => ActivityId(id)
  }

  def serializeActivityId(implicit formats: Formats): PartialFunction[Any, JValue] = {
    case ActivityId(id) => JInt(id)
  }

  object activityIdSerializer extends CustomSerializer[ActivityId](
    formats =>
      (deserializeActivityId(formats), serializeActivityId(formats)))

  def deserializeActivityLink(implicit formats: Formats): PartialFunction[JValue, ActivityLink] = {
    case j: JObject =>
      ActivityLink((j \ "id").extract[BigInt], (j \ "name").extract[String])
  }

  object activityLinkSerializer extends CustomSerializer[ActivityLink](
    formats => deserializeActivityLink(formats) -> PartialFunction.empty)

  def deserializeActivity(implicit formats: Formats): PartialFunction[JValue, Activity] = {
    case j: JObject =>
      Activity(
        (j \ "id").extract[BigInt],
        (j \ "name").extract[String],
        (j \ "custom_fields").toOption.map(_.extract[Set[CustomField]]))
  }

  object activitySerializer extends CustomSerializer[Activity](
    formats => deserializeActivity(formats) -> PartialFunction.empty)

}