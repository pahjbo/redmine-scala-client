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
import org.json4s.JsonDSL._

import scala.collection.immutable._

object ProjectSerializers {
  lazy val all: Seq[Serializer[_]] = Seq(
    projectIdSerializer, projectLinkSerializer, projectSerializer, newProjectSerializer, projectUpdateSerializer)

  def deserializeProjectId(implicit formats: Formats): PartialFunction[JValue, ProjectId] = {
    case JInt(id) => ProjectId(id)
  }

  def serializeProjectId(implicit formats: Formats): PartialFunction[Any, JValue] = {
    case ProjectId(id) => JInt(id)
  }

  object projectIdSerializer
    extends CustomSerializer[ProjectId](
      formats => (
        deserializeProjectId(formats), serializeProjectId(formats)))

  def deserializeProjectLink(implicit formats: Formats): PartialFunction[JValue, ProjectLink] = {
    case j: JObject =>
      val id = (j \ "id").extract[BigInt]
      val name = (j \ "name").extract[String]
      ProjectLink(id, name)
  }

  def serializeProjectLink(implicit formats: Formats): PartialFunction[Any, JValue] = {
    case ProjectLink(id, name) => ("id" -> id) ~ ("name" -> name)
  }

  object projectLinkSerializer
    extends CustomSerializer[ProjectLink](
      formats => (
        deserializeProjectLink(formats), serializeProjectLink(formats)))

  def deserializeProject(implicit formats: Formats): PartialFunction[JValue, Project] = {
    case j: JObject =>
      val id = (j \ "id").extract[BigInt]
      val identifier = (j \ "identifier").extract[String]
      val name = (j \ "name").extract[String]
      val parent = (j \ "parent").extractOpt[ProjectLink]
      val description = (j \ "description").extractOpt[String]
      val homepage = (j \ "homepage").extractOpt[String]
      val createdOn = RedmineDateParser.parse((j \ "created_on").extract[String])
      val updatedOn = RedmineDateParser.parse((j \ "updated_on").extract[String])
      val isPublic = (j \ "is_public").extractOpt[Boolean]
      val customFields = (j \ "custom_fields").toOption.map(_.extract[Set[CustomField]])
      val trackers = (j \ "trackers").toOption.map(_.extract[Set[TrackerLink]])
      val issueCategories = (j \ "issue_categories").toOption.map(_.extract[Set[CategoryLink]])
      val enabledModules = (j \ "enabled_module_names").toOption.map(_.extract[Set[ModuleLink]])
      new Project(
        id, identifier, parent, name, description, homepage, createdOn, updatedOn, isPublic, customFields, trackers,
        issueCategories, enabledModules)
  }

  object projectSerializer
    extends CustomSerializer[Project](
      formats => (
        deserializeProject(formats), PartialFunction.empty))

  def serializeNewProject(implicit formats: Formats): PartialFunction[Any, JValue] = {
    case p: Project.New =>
      ("name" -> p.name) ~
        ("identifier" -> p.identifier) ~
        ("description" -> p.description.toOpt) ~
        ("homepage" -> p.homepage.toOpt) ~
        ("is_public" -> p.isPublic.toOpt) ~
        ("parent_id" -> p.parent.toOpt.map(_.id)) ~
        ("inherit_members" -> p.inheritMembers.toOpt) ~
        ("tracker_ids" -> p.trackers.toOpt.map(_.map(_.id))) ~
        ("enabled_module_names" -> p.enabledModuleNames.toOpt) ~
        ("custom_fields" -> p.customFields.toOpt.map(_.map(Extraction.decompose)))
  }

  object newProjectSerializer
    extends CustomSerializer[Project.New](
      formats => (
        PartialFunction.empty, serializeNewProject(formats)))

  def serializeProjectUpdate(implicit formats: Formats): PartialFunction[Any, JValue] = {
    case p: Project.Update =>
      ("name" -> p.name.toOpt) ~
        ("description" -> p.description.toOpt.map(_.map(Extraction.decompose).getOrElse(JNull)).getOrElse(JNothing)) ~
        ("homepage" -> p.homepage.toOpt.map(_.map(Extraction.decompose).getOrElse(JNull)).getOrElse(JNothing)) ~
        ("is_public" -> p.isPublic.toOpt) ~
        ("parent_id" ->
          p.parent.toOpt.map(_.map(v => Extraction.decompose(v.id.toString())).getOrElse(JNull)).getOrElse(JNothing)) ~
        ("inherit_members" -> p.inheritMembers.toOpt) ~
        ("tracker_ids" -> p.trackers.toOpt.map(_.map(Extraction.decompose))) ~
        ("enabled_module_names" -> p.enabledModuleNames.toOpt.map(_.map(Extraction.decompose))) ~
        ("custom_fields" -> p.customFields.toOpt.map(_.map(Extraction.decompose)))
  }

  object projectUpdateSerializer
    extends CustomSerializer[Project.Update](
      formats => (
        PartialFunction.empty, serializeProjectUpdate(formats)))

}
