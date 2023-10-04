package de.jokileda.shared.sql;

import de.jokileda.core.api.shared.sql.ISql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class MysqlHandler implements ISql {

  private final Connection connection;

  public MysqlHandler(Connection connection) {
    this.connection = connection;
  }

  @Override
  public CompletableFuture<Object> executeQuery(String query,
      Object parameterObject, String column) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        PreparedStatement preparedStatement = connection.prepareStatement(
            query);
        preparedStatement.setObject(1, parameterObject);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
          return resultSet.getObject(column);
        }
        return null;
      } catch (SQLException executeQueryError) {
        throw new RuntimeException("Error: ", executeQueryError);
      }
    });
  }
  @Override
  public CompletableFuture<Boolean> executeQuery(String query,
      Object parameterObject) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        PreparedStatement preparedStatement = connection.prepareStatement(
            query);
        preparedStatement.setObject(1, parameterObject);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
      } catch (SQLException executeQueryError) {
        executeQueryError.printStackTrace();
        throw new RuntimeException("Error: ", executeQueryError);
      }
    });
  }

  @Override
  public CompletableFuture<ResultSet> executeQueryResultSet(String query, Object parameterObject) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        PreparedStatement preparedStatement = connection.prepareStatement(
                query);
        preparedStatement.setObject(1, parameterObject);
          return preparedStatement.executeQuery();
      } catch (SQLException executeQueryError) {
        throw new RuntimeException("Error: ", executeQueryError);
      }
    });
  }

  @Override
  public CompletableFuture<ResultSet> executeQueryResultSet(String query) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        PreparedStatement preparedStatement = connection.prepareStatement(
                query);
        return preparedStatement.executeQuery();
      } catch (SQLException executeQueryError) {
        throw new RuntimeException("Error: ", executeQueryError);
      }
    });
  }

  @Override
  public void preparedStatementWithObjects(String query,
      Object... objects) {
    CompletableFuture.supplyAsync(() -> {
      try {
        PreparedStatement preparedStatement = connection.prepareStatement(
            query);
        int parameterIndex = 1;
        for (Object o : objects) {
          preparedStatement.setObject(parameterIndex, o);
          parameterIndex++;
        }
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("Rows affected: " + parameterIndex);
        return true;
      } catch (SQLException executePreparedStatementError) {
        executePreparedStatementError.printStackTrace();
        throw new RuntimeException("Error: ", executePreparedStatementError);
      }
    });
  }
}
