// SPDX-License-Identifier: LGPL-2.1-or-later
// Copyright (c) 2012-2014 Monty Program Ab
// Copyright (c) 2015-2021 MariaDB Corporation Ab

package org.mariadb.jdbc.message.client;

import java.io.IOException;
import java.sql.SQLException;
import org.mariadb.jdbc.client.Context;
import org.mariadb.jdbc.client.socket.Writer;
import org.mariadb.jdbc.client.util.Parameter;
import org.mariadb.jdbc.client.util.Parameters;
import org.mariadb.jdbc.plugin.codec.ByteArrayCodec;
import org.mariadb.jdbc.util.ClientParser;

public final class QueryWithParametersPacket implements RedoableClientMessage {

  private final String preSqlCmd;
  private final ClientParser parser;
  private Parameters parameters;

  public QueryWithParametersPacket(String preSqlCmd, ClientParser parser, Parameters parameters) {
    this.preSqlCmd = preSqlCmd;
    this.parser = parser;
    this.parameters = parameters;
  }

  @Override
  public void ensureReplayable(Context context) throws IOException, SQLException {
    int parameterCount = parameters.size();
    for (int i = 0; i < parameterCount; i++) {
      Parameter p = parameters.get(i);
      if (!p.isNull() && p.canEncodeLongData()) {
        this.parameters.set(
            i, new org.mariadb.jdbc.codec.Parameter<>(ByteArrayCodec.INSTANCE, p.encodeData()));
      }
    }
  }

  public void saveParameters() {
    this.parameters = this.parameters.clone();
  }

  @Override
  public int encode(Writer encoder, Context context) throws IOException, SQLException {
    encoder.initPacket();
    encoder.writeByte(0x03);
    if (!preSqlCmd.isEmpty()) encoder.writeAscii(preSqlCmd);
    if (parser.getParamCount() == 0) {
      encoder.writeBytes(parser.getQueryParts().get(0));
    } else {
      encoder.writeBytes(parser.getQueryParts().get(0));
      for (int i = 0; i < parser.getParamCount(); i++) {
        if (parameters.get(i).isNull()) {
          encoder.writeAscii("null");
        } else {
          parameters.get(i).encodeText(encoder, context);
        }
        encoder.writeBytes(parser.getQueryParts().get(i + 1));
      }
    }
    encoder.flush();
    return 1;
  }

  public int batchUpdateLength() {
    return 1;
  }

  @Override
  public String description() {
    return parser.getSql();
  }
}
