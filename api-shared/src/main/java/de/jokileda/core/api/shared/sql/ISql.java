package de.jokileda.core.api.shared.sql;

import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public interface ISql {

  CompletableFuture<Object> executeQuery(String query, Object parameterObject, String column);

  CompletableFuture<Boolean> executeQuery(String query, Object parameterObject);

  CompletableFuture<ResultSet> executeQueryResultSet(String query, Object parameterObject);

  CompletableFuture<ResultSet> executeQueryResultSet(String query);

  void preparedStatementWithObjects(String query, Object... objects);

}
