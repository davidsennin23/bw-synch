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
package org.bedework.synch.shared;

import org.bedework.synch.shared.exception.SynchException;
import org.bedework.util.misc.ToString;
import org.bedework.util.misc.Util;

import java.util.List;

/** Provides an internal deserialized view of the subscription info for one
 * end. A number of common methods are provided here and it is assumed that
 * connectors and instances will subclass this class.
 *
 * @author Mike Douglass
 */
public class BaseSubscriptionInfo {
  private final SubscriptionConnectorInfo info;

  /* properties saved by connector instance */

  /** A uri referring to the endpoint. Perhaps the URL of a file or the href
   * for a caldav collection
   */
  public static final String propnameUri = "uri";

  /** The value of some sort of change token. This could be a caldav ctag or an
   * http etag.
   */
  public static final String propnameChangeToken = "ctoken";

  /** Opaque data for this connection */
  public static final String propnameOpaqueData = "opaque-data";

  /** A principal - possibly an href or an account */
  public static final String propnamePrincipal = "principal";

  /** The (encoded) password for the principal */
  public static final String propnamePassword = "password";

  /** Refresh period for polling subscriptions (millisecs) */
  public static final String propnameRefreshDelay = "refreshDelay";

  /** Allow only public entities */
  public static final String propnameOrgSyncPublicOnly = "orgsync-publicOnly";

  /** Location key name for matching */
  public static final String propnameLocKey = "locKey";

  /** A string value that provides information about the last refresh for this
   * end of the subscription
   */
  public static final String propnameLastRefreshStatus = "last-refresh-status";

  /** The numbers created, updated, deleted last time */
  public static final String propnameLastCrudCts = "lastCrudCt";

  /** The numbers created, updated, deleted this subscription */
  public static final String propnameTotalCrudCts = "totalCrudCt";

  /** Comma separatd list of input filter property classes */
  public static final String propnameInputFilterClasses = "inFilterClasses";

  /** Comma separated list of output filter property classes */
  public static final String propnameOutputFilterClasses = "outFilterClasses";

  /** maintain some counts
   */
  public static class CrudCts {
    /** The counts
     */
    public long created;
    /** The counts
     */
    public long updated;
    /** The counts
     */
    public long deleted;

    /** Deserialize
     * @param val - serialized counts
     * @return cts
     */
    public static CrudCts fromString(final String val) {
      final CrudCts cc = new CrudCts();

      if (val == null) {
        return cc;
      }

      final String[] cts = val.split(",");

      if (cts.length != 3) {
        return cc;
      }

      try {
        cc.created = Long.valueOf(cts[0]);
        cc.updated = Long.valueOf(cts[1]);
        cc.deleted = Long.valueOf(cts[2]);
      } catch (final Throwable ignored) {
      }

      return cc;
    }

    @Override
    public String toString() {
      return String.valueOf(created) + "," + deleted + "," + updated;
    }
  }

  /**
   * @param info subscription connector info
   * @throws SynchException
   */
  public BaseSubscriptionInfo(final SubscriptionConnectorInfo info) throws SynchException {
    this.info = info;
    info.loadProperties();
  }

  /** Path to the calendar collection.
   *
   * @param val    String
   * @throws SynchException
   */
  public void setUri(final String val) throws SynchException {
    info.setProperty(propnameUri, val);
  }

  /** Path to the calendar collection
   *
   * @return String
   * @throws SynchException
   */
  public String getUri() throws SynchException {
    return info.getProperty(propnameUri);
  }

  /** Principal requesting synch service
   *
   * @param val    String
   * @throws SynchException
   */
  public void setPrincipalHref(final String val) throws SynchException {
    info.setProperty(propnamePrincipal, val);
  }

  /** Principal requesting synch service
   *
   * @return String
   * @throws SynchException
   */
  public String getPrincipalHref() throws SynchException {
    return info.getProperty(propnamePrincipal);
  }

  /** Principals password
   *
   * @param val    String
   * @throws SynchException
   */
  public void setPassword(final String val) throws SynchException {
    info.setProperty(propnamePassword, val);
  }

  /** Principal password
   *
   * @return String
   * @throws SynchException
   */
  public String getPassword() throws SynchException {
    return info.getProperty(propnamePassword);
  }

  /** Opaque data for the connection
   *
   * @param val    String
   * @throws SynchException
   */
  public void setOpaqueData(final String val) throws SynchException {
    info.setProperty(propnameOpaqueData, val);
  }

  /** Opaque data for the connection
   *
   * @return String
   * @throws SynchException
   */
  public String getOpaqueData() throws SynchException {
    return info.getProperty(propnameOpaqueData);
  }

  /** ChangeToken
   *
   * @param val    String
   * @throws SynchException
   */
  public void setChangeToken(final String val) throws SynchException {
    info.setProperty(propnameChangeToken, val);
  }

  /** ChangeToken
   *
   * @return String
   * @throws SynchException
   */
  public String getChangeToken() throws SynchException {
    return info.getProperty(propnameChangeToken);
  }

  /** HTTP status or other appropriate value
   * @param val status
   * @throws SynchException
   */
  public void setLastRefreshStatus(final String val) throws SynchException {
    info.setProperty(propnameLastRefreshStatus, val);
  }

  /**
   * @return String lastRefreshStatus
   * @throws SynchException
   */
  public String getLastRefreshStatus() throws SynchException {
    return info.getProperty(propnameLastRefreshStatus);
  }

  /**
   * @param val crud counts
   * @throws SynchException
   */
  public void setLastCrudCts(final CrudCts val) throws SynchException {
    info.setProperty(propnameLastCrudCts, val.toString());
  }

  /**
   * @return cts
   * @throws SynchException
   */
  public CrudCts getLastCrudCts() throws SynchException {
    String s = info.getProperty(propnameLastCrudCts);


    CrudCts cc = CrudCts.fromString(s);

    if (s == null) {
      setLastCrudCts(cc); // Ensure they get saved
    }

    return cc;
  }

  /**
   * @param val crud counts
   * @throws SynchException
   */
  public void setTotalCrudCts(final CrudCts val) throws SynchException {
    info.setProperty(propnameTotalCrudCts, val.toString());
  }

  /**
   * @return cts
   * @throws SynchException
   */
  public CrudCts getTotalCrudCts() throws SynchException {
    String s = info.getProperty(propnameTotalCrudCts);


    CrudCts cc = CrudCts.fromString(s);

    if (s == null) {
      setTotalCrudCts(cc); // Ensure they get saved
    }

    return cc;
  }

  /** Refresh delay - millisecs
   *
   * @param val millisecs
   * @throws SynchException
   */
  public void setRefreshDelay(final String val) throws SynchException {
    info.setProperty(propnameRefreshDelay, val);
  }

  /** Refresh delay - millisecs
   *
   * @return String refreshDelay
   * @throws SynchException
   */
  public String getRefreshDelay() throws SynchException {
    return info.getProperty(propnameRefreshDelay);
  }

  /** set arbitrary named property
   * @param name of property
   * @param val - String property value
   * @throws SynchException
   */
  public void setProperty(final String name, final String val) throws SynchException {
    info.setProperty(name, val);
  }

  /** Get arbitrary named property
   * @param name of property
   * @return String property value
   * @throws SynchException
   */
  public String getProperty(final String name) throws SynchException {
    return info.getProperty(name);
  }

  /**
   * @param classes ordered list of class names
   * @throws SynchException
   */
  public void setInFilterClasses(List<String> classes) throws SynchException {
    info.setProperty(propnameInputFilterClasses, asString(classes));
  }

  /**
   * @return ordered list of class names
   * @throws SynchException
   */
  public List<String> getInFilterClasses() throws SynchException {
    try {
      return Util.getList(info.getProperty(
                                  propnameInputFilterClasses),
                          false);
    } catch (final Throwable t) {
      throw new SynchException(t);
    }
  }

  /**
   * @param classes ordered list of class names
   * @throws SynchException
   */
  public void setOutFilterClasses(List<String> classes) throws SynchException {
    info.setProperty(propnameOutputFilterClasses, asString(classes));
  }

  /**
   * @return ordered list of class names
   * @throws SynchException
   */
  public List<String> getOutFilterClasses() throws SynchException {
    try {
      return Util.getList(info.getProperty(
                                  propnameOutputFilterClasses),
                          false);
    } catch (final Throwable t) {
      throw new SynchException(t);
    }
  }

  /* ====================================================================
   *                   Convenience methods
   * ==================================================================== */

  private String asString(List<String> vals) {
    final StringBuilder sb = new StringBuilder();
    String delim = "";

    for (final String s: vals) {
      sb.append(delim);
      sb.append(s);

      delim = ",";
    }

    return sb.toString();
  }

  protected void toStringSegment(final ToString ts) {
    try {
      ts.append("uri", getUri());
      ts.newLine();
      ts.append("principalHref", getPrincipalHref());
      ts.append("password", getPassword());
      ts.append("etag", getChangeToken());
      ts.newLine();
      ts.append("lastRefreshStatus", getLastRefreshStatus());
      ts.newLine();
      ts.append("lastCrudCts", getLastCrudCts());
      ts.append("totalCrudCts", getTotalCrudCts());
      ts.append("refreshDelay", getRefreshDelay());
    } catch (Throwable t) {
      ts.append(t.getMessage());
    }
  }

  /* ====================================================================
   *                        Object methods
   * ==================================================================== */

  @Override
  public String toString() {
    final ToString ts = new ToString(this);

    toStringSegment(ts);

    return ts.toString();
  }
}
