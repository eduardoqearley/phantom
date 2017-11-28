/*
 * Copyright 2013 - 2017 Outworkers Ltd.
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
package com.outworkers.phantom.example.advanced

import java.util.UUID

import com.outworkers.phantom.dsl._

import scala.concurrent.{Future => ScalaFuture}

// Now you want to enable querying Recipes by author.
// Because of the massive performance overhead of filtering,
// you can't really use a SecondaryKey for multi-billion record databases.

// Instead, you create mapping tables and ensure consistency from the application level.
// This will illustrate just how easy it is to do that with com.outworkers.phantom.
abstract class AdvancedRecipesByTitle extends Table[AdvancedRecipesByTitle, (String, UUID)] {

  // In this table, the author will be PrimaryKey and PartitionKey.
  object title extends StringColumn with PartitionKey

  // The id is just another normal field.
  object id extends UUIDColumn

  override lazy val tableName = "recipes_by_title"

  // now you can have the tile in a where clause
  // without the performance impact of a secondary index.
  def findRecipeByTitle(title: String): ScalaFuture[Option[(String, UUID)]] = {
    select.where(_.title eqs title).one()
  }
}
