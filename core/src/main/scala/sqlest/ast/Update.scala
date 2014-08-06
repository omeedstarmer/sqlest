/*
 * Copyright 2014 JHC Systems Limited
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

package sqlest.ast

/** An update statement. */
case class Update(table: Table, set: Seq[Setter[_, _]], where: Option[Column[Boolean]] = None) extends Command {
  def set(setters: Setter[_, _]*) =
    this.copy(set = this.set ++ setters)

  def where(expr: Column[Boolean]): Update =
    this.copy(where = this.where map (_ && expr) orElse Some(expr))
}