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
package com.outworkers.phantom.tables.bugs

import com.outworkers.phantom.dsl._

abstract class AccountInviteModel extends Table[AccountInviteModel, AccountInvite] {

  override def tableName: String = "account_invite"

  object accountId extends StringColumn with PartitionKey
  object email extends StringColumn with PartitionKey

  object role extends StringColumn
  object isRegistered extends BooleanColumn
  object firstName extends StringColumn
  object lastName extends StringColumn
  object entitlements extends ListColumn[String]
  object allDocs extends BooleanColumn
  object docs extends ListColumn[String]
  object status extends StringColumn

  def find(accountId: String, email: String) = {
    select.where(_.accountId eqs accountId).and(_.email eqs email).one()
  }

  def setAccepted(accountId: String, email: String) = {
    update.where(_.accountId eqs accountId).and(_.email eqs email).modify(_.status setTo "ACCEPTED").future()
  }

  def inviteToAccount(accountId: String, isRegistered: Boolean, accountInvite: AccountInvite) = {
    store(accountInvite).ifNotExists().future().map(_.wasApplied())
  }

}
