package de.jokileda.shared.permission;

import de.jokileda.core.api.shared.permission.IPermissionGroup;
import de.jokileda.shared.SharedLoader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PermissionGroupHandler implements IPermissionGroup {

  private final SharedLoader sharedLoader;

  public PermissionGroupHandler(SharedLoader sharedLoader) {
    this.sharedLoader = sharedLoader;
  }

  @Override
  public void createOpUpdateGroup(String name, String prefix, int priority,
      List<String> permission) {
    sharedLoader.iSql.preparedStatementWithObjects(
        "INSERT INTO permission_group (group_name,priority,permissions,prefix,score_team) VALUES (?,?,?,?,?)"
            + " ON DUPLICATE KEY UPDATE group_name = ?, priority = ?, permissions = ?, prefix = ?, score_team = ?",
        name, priority, extractPermissionsFromList(permission), prefix, name, priority, extractPermissionsFromList(permission), prefix);
  }

  @Override
  public void deleteGroup(String name) {
    if (groupExist(name)) {
      sharedLoader.iSql.preparedStatementWithObjects(
          "DELETE FROM permission_group WHERE group_name = ?", name);
    }
  }

  @Override
  public String getGroupPrefix(String name) {
    CompletableFuture<Object> future = sharedLoader.iSql.executeQuery(
        "SELECT * FROM permission_group WHERE group_name = ?", name, "prefix");
    String prefix;
    try {
      prefix = (String) future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return prefix;
  }

  @Override
  public String getTabTeam(String name) {
    CompletableFuture<Object> future = sharedLoader.iSql.executeQuery(
        "SELECT * FROM permission_group WHERE group_name = ?", name, "score_team");
    String prefix;
    try {
      prefix = (String) future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return prefix;
  }

  @Override
  public List<String> getGroupPermissions(List<String> groups) {
    List<String> permissions = new ArrayList<>();
    groups.forEach(s -> {
      CompletableFuture<Object> groupsFuture = new CompletableFuture<>();
      String[] groupsString = s.split(", ");
      for (String group : groupsString) {
        groupsFuture = sharedLoader.iSql.executeQuery(
            "SELECT * FROM permission_group WHERE group_name = ?", group,
            "permissions");
      }
      try {
        permissions.add((String) groupsFuture.get());
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
    });
    return permissions;
  }

  @Override
  public List<String> allGroups() {
    List<String> groups = new ArrayList<>();
    CompletableFuture<ResultSet> future = sharedLoader.iSql.executeQueryResultSet("SELECT * FROM permission_group");
    try {
      ResultSet resultSet = future.get();
      while (resultSet.next()) {
        groups.add(resultSet.getString("group_name"));
      }
    } catch (SQLException | ExecutionException | InterruptedException e) {
      throw new RuntimeException(e);
    }
    return groups;
  }

  @Override
  public boolean groupExist(String name) {
    CompletableFuture<Boolean> future = sharedLoader.iSql.executeQuery(
        "SELECT * FROM permission_group WHERE group_name = ?", name);
    boolean exist;
    try {
      exist = future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return exist;
  }

  private String extractPermissionsFromList(List<String> permissions) {
    StringBuilder stringBuilder = new StringBuilder();
    for (String permission : permissions) {
      stringBuilder.append(permission);
      stringBuilder.append(" ");
    }
    return stringBuilder.toString();
  }
}
