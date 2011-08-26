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
package org.bedework.synch.cnctrs.file;

import org.bedework.synch.Notification;
import org.bedework.synch.Subscription;
import org.bedework.synch.SynchDefs.SynchEnd;
import org.bedework.synch.SynchDefs.SynchKind;
import org.bedework.synch.SynchEngine;
import org.bedework.synch.cnctrs.Connector;
import org.bedework.synch.cnctrs.ConnectorInstanceMap;
import org.bedework.synch.cnctrs.ConnectorPropertyInfo;
import org.bedework.synch.exception.SynchException;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** The synch processor connector for subscriptions to files.
 *
 * @author Mike Douglass
 */
public class FileConnector
      implements Connector<FileConnectorInstance,
                           Notification> {
  private transient Logger log;

  /** */
  public static final String propnameUri = "uri";

  /** lastmod in the file is accurate */
  public static final String propnameUseLastmod = "use-lastmod";

  /** */
  public static final String propnamePrincipal = "principal";

  /** */
  public static final String propnamePassword = "password";

  private static List<ConnectorPropertyInfo> propInfo =
      new ArrayList<ConnectorPropertyInfo>();

  static {
    propInfo.add(new ConnectorPropertyInfo(propnameUri,
                                           false,
                                           ""));

    propInfo.add(new ConnectorPropertyInfo(propnamePrincipal,
                                           false,
                                           ""));

    propInfo.add(new ConnectorPropertyInfo(propnamePassword,
                                           true,
                                           ""));
  }

  private SynchEngine syncher;

  private FileConnectorConfig config;

  private String callbackUri;

  private String connectorId;

  private long keepAliveInterval = 10 * 1000;

  private ConnectorInstanceMap<FileConnectorInstance> cinstMap =
      new ConnectorInstanceMap<FileConnectorInstance>();

  @Override
  public void start(final String connectorId,
                    final String callbackUri,
                    final SynchEngine syncher) throws SynchException {
    this.connectorId = connectorId;
    this.syncher = syncher;
    this.callbackUri = callbackUri;

    config = (FileConnectorConfig)syncher.getAppContext().getBean(
                                  connectorId + "FileConfig");

    this.syncher = syncher;
  }

  @Override
  public SynchKind getKind() {
    return SynchKind.poll;
  }

  @Override
  public String getId() {
    return connectorId;
  }

  @Override
  public String getCallbackUri() {
    return callbackUri;
  }

  @Override
  public SynchEngine getSyncher() {
    return syncher;
  }

  @Override
  public List<ConnectorPropertyInfo> getPropertyInfo() {
    return propInfo;
  }

  @Override
  public FileConnectorInstance getConnectorInstance(final Subscription sub,
                                                        final SynchEnd end) throws SynchException {
    FileConnectorInstance inst = cinstMap.find(sub, end);

    if (inst != null) {
      return inst;
    }

    //debug = getLogger().isDebugEnabled();
    FileSubscriptionInfo info;

    if (end == SynchEnd.endA) {
      info = new FileSubscriptionInfo(sub.getEndAConnectorInfo());
    } else {
      info = new FileSubscriptionInfo(sub.getEndBConnectorInfo());
    }

    inst = new FileConnectorInstance(config, this, sub, end, info);
    cinstMap.add(sub, end, inst);

    return inst;
  }

  class BedeworkNotificationBatch extends NotificationBatch<Notification> {
  }

  @Override
  public BedeworkNotificationBatch handleCallback(final HttpServletRequest req,
                                     final HttpServletResponse resp,
                                     final List<String> resourceUri) throws SynchException {
    return null;
  }

  @Override
  public void respondCallback(final HttpServletResponse resp,
                              final NotificationBatch<Notification> notifications)
                                                    throws SynchException {
  }

  @Override
  public void stop() throws SynchException {
  }

  /* ====================================================================
   *                   Protected methods
   * ==================================================================== */

  protected void info(final String msg) {
    getLogger().info(msg);
  }

  protected void trace(final String msg) {
    getLogger().debug(msg);
  }

  protected void error(final Throwable t) {
    getLogger().error(this, t);
  }

  protected void error(final String msg) {
    getLogger().error(msg);
  }

  protected void warn(final String msg) {
    getLogger().warn(msg);
  }

  /* Get a logger for messages
   */
  protected Logger getLogger() {
    if (log == null) {
      log = Logger.getLogger(this.getClass());
    }

    return log;
  }

  /* ====================================================================
   *                         Package methods
   * ==================================================================== */

  /* ====================================================================
   *                   Private methods
   * ==================================================================== */
}