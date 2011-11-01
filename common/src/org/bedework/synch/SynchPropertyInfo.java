/* ********************************************************************
    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.
*/
package org.bedework.synch;

import org.bedework.synch.wsmessages.SynchPropertyInfoType;

/** Information about a single connector property. This information will be
 * published by the system allowing clients to determine what properties are
 * needed.
 *
 * @author Mike Douglass
 */
public class SynchPropertyInfo extends SynchPropertyInfoType {
  /**
   * @param name - name for the property
   * @param secure - true if this property value should be hidden, e.g password
   * @param description - of the property
   * @param required - true if this property is required
   */
  public SynchPropertyInfo(final String name,
                               final boolean secure,
                               final String description,
                               final boolean required) {
    setName(name);
    setSecure(secure);
    setDescription(description);
    setRequired(required);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append("{");

    sb.append("name = ");
    sb.append(getName());

    sb.append(", secure = ");
    sb.append(isSecure());

    sb.append(",\n   description = ");
    sb.append(getDescription());

    sb.append(",\n   required = ");
    sb.append(isRequired());

    sb.append("}");
    return sb.toString();
  }

}
