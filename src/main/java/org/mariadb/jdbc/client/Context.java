// SPDX-License-Identifier: LGPL-2.1-or-later
// Copyright (c) 2012-2014 Monty Program Ab
// Copyright (c) 2015-2021 MariaDB Corporation Ab

package org.mariadb.jdbc.client;

import org.mariadb.jdbc.Configuration;
import org.mariadb.jdbc.export.ExceptionFactory;

public interface Context {

  long getThreadId();

  byte[] getSeed();

  long getServerCapabilities();

  int getServerStatus();

  void setServerStatus(int serverStatus);

  String getDatabase();

  void setDatabase(String database);

  ServerVersion getVersion();

  boolean isEofDeprecated();

  boolean canSkipMeta();

  boolean isExtendedInfo();

  int getWarning();

  void setWarning(int warning);

  ExceptionFactory getExceptionFactory();

  Configuration getConf();

  int getTransactionIsolationLevel();

  void setTransactionIsolationLevel(int transactionIsolationLevel);

  PrepareCache getPrepareCache();

  void resetPrepareCache();

  int getStateFlag();

  void resetStateFlag();

  void addStateFlag(int state);
}
