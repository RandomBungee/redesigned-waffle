package de.jokileda.shared.config;

import de.jokileda.core.api.shared.IPlugin;
import de.jokileda.core.api.shared.util.PluginType;
import de.jokileda.core.api.proxy.config.ProxyConfigManager;
import de.jokileda.core.api.spigot.config.SpigotConfigManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnectionConfig {

  private final IPlugin iPlugin;
  public MysqlConnectionConfig(IPlugin iPlugin) {
    this.iPlugin = iPlugin;
  }

  public Connection getConnection()
      throws SQLException {
    Connection connection;
    String host;
    String port;
    String database;
    String user;
    String password;
    if(iPlugin.getPluginType() == PluginType.SPIGOT) {
      SpigotConfigManager spigotConfigManager = new SpigotConfigManager("core", "mysql");
      host = spigotConfigManager.getString("core.mysql.host", "127.0.0.1");
      port = spigotConfigManager.getString("core.mysql.port", "3360");
      database = spigotConfigManager.getString("core.mysql.database", "db");
      user = spigotConfigManager.getString("core.mysql.user", "root");
      password = spigotConfigManager.getString("core.mysql.password", "password");
    } else {
      ProxyConfigManager proxyConfigManager = new ProxyConfigManager("core", "mysql");
      host = proxyConfigManager.getString("core.mysql.host", "127.0.0.1");
      port = proxyConfigManager.getString("core.mysql.port", "3360");
      database = proxyConfigManager.getString("core.mysql.database", "db");
      user = proxyConfigManager.getString("core.mysql.user", "root");
      password = proxyConfigManager.getString("core.mysql.password", "password");
    }
    connection = DriverManager.getConnection(
        "jdbc:mysql://" + host + ":" + port + "/" + database
            + "?autoReconnect=true",
            user, password);
    return connection;
  }
}
