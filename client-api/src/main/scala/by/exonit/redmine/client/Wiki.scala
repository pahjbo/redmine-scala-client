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

package by.exonit.redmine.client

import org.joda.time.DateTime

trait WikiPageIdLike extends Identifiable[String]

case class WikiPageId(id: String) extends WikiPageIdLike

trait WikiPageLike extends WikiPageIdLike {
  def version: BigInt
  def createdOn: DateTime
  def updatedOn: DateTime
  def parent: Option[WikiPageIdLike]
}

case class WikiPage(
  id: String,
  version: BigInt,
  createdOn: DateTime,
  updatedOn: DateTime,
  parent: Option[WikiPageId]
) extends WikiPageLike

case class WikiPageDetails(
  id: String,
  version: BigInt,
  createdOn: DateTime,
  updatedOn: DateTime,
  parent: Option[WikiPageId],
  text: String,
  author: Option[UserLink],
  comments: Option[String]
) extends WikiPageLike

object WikiPage {
  case class New(
    title: String,
    text: String,
    parent: Option[WikiPageIdLike] = None,
    comments: Option[String] = None
  )

  case class Update(
    text: Option[String] = None,
    parent: Option[Option[WikiPageIdLike]] = None,
    comments: Option[String] = None
  )
}
