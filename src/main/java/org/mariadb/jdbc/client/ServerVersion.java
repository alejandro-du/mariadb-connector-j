// SPDX-License-Identifier: LGPL-2.1-or-later
// Copyright (c) 2012-2014 Monty Program Ab
// Copyright (c) 2015-2021 MariaDB Corporation Ab

package org.mariadb.jdbc.client;

public interface ServerVersion {

  String getVersion();

  int getMajorVersion();

  int getMinorVersion();

  int getPatchVersion();

  String getQualifier();

  /**
   * Utility method to check if database version is greater than parameters.
   *
   * @param major major version
   * @param minor minor version
   * @param patch patch version
   * @return true if version is greater than parameters
   */
  boolean versionGreaterOrEqual(int major, int minor, int patch);

  boolean isMariaDBServer();
}
