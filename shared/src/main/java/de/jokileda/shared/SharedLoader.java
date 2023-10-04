package de.jokileda.shared;

import de.jokileda.core.api.shared.IPlugin;
import de.jokileda.core.api.shared.sql.ISql;
import de.jokileda.shared.config.MysqlConnectionConfig;
import de.jokileda.shared.sql.MysqlHandler;
import java.sql.SQLException;
import lombok.Data;

@Data
public class SharedLoader {

  private MysqlHandler mysqlHandler;
  private MysqlConnectionConfig mysqlConnectionConfig;
  public ISql iSql;

  public void loadShadedApi(IPlugin iPlugin) {
    try {
      mysqlConnectionConfig = new MysqlConnectionConfig(iPlugin);
      mysqlHandler = new MysqlHandler(mysqlConnectionConfig.getConnection());
      iSql = mysqlHandler;
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
