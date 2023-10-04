package de.jokileda.shared.permission;

import de.jokileda.core.api.shared.permission.IPermissionUser;
import de.jokileda.shared.SharedLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PermissionUserHandler implements IPermissionUser {

  private final SharedLoader sharedLoader;

  public PermissionUserHandler(SharedLoader sharedLoader) {
    this.sharedLoader = sharedLoader;
  }

  @Override
  public void createPermissionUser(String uuid, String group,
      List<String> permissions) {
    if (userExist(uuid)) {
      return;
    }
    List<String> groups = new ArrayList<>();
    groups.add(group);
    sharedLoader.iSql.preparedStatementWithObjects(
        "INSERT INTO permission_user (unique_id,groups,group_time,permissions) VALUES (?,?,?,?)",
        uuid, extractFromList(groups), -1, extractFromList(permissions));
  }

  @Override
  public void updateUser(String uuid, String group,
      long groupTime,
      boolean overrideGroup) {
    List<String> groupsOfUser = getUserLists(uuid, "groups");
    if (overrideGroup) {
      groupsOfUser.clear();
    }
    groupsOfUser.add(group);
    sharedLoader.iSql.preparedStatementWithObjects(
        "UPDATE permission_user SET groups = ?, group_time = ? WHERE unique_id = ?",
        extractFromList(groupsOfUser), -1, uuid);
  }

  @Override
  public void updateUser(String uuid, String permissions) {
    List<String> permsOfUser = getUserLists(uuid, "permissions");
    permsOfUser.add(permissions);
    sharedLoader.iSql.preparedStatementWithObjects(
            "UPDATE permission_user SET permissions = ? WHERE unique_id = ?",
            extractFromList(permsOfUser), uuid);
  }

  @Override
  public List<String> getUserLists(String uuid, String column) {
    List<String> list = new ArrayList<>();
    CompletableFuture<Object> groupsFuture = sharedLoader.iSql.executeQuery(
        "SELECT * FROM permission_user WHERE unique_id = ?", uuid, column);
    try {
      list.add((String) groupsFuture.get());
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return list;
  }

  @Override
  public boolean userExist(String uuid) {
    CompletableFuture<Boolean> future = sharedLoader.iSql.executeQuery(
        "SELECT * FROM permission_user WHERE unique_id = ?", uuid);
    boolean exist;
    try {
      exist = future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
    return exist;
  }

  private String extractFromList(List<String> list) {
    StringBuilder stringBuilder = new StringBuilder();
    for (String permission : list) {
      stringBuilder.append(permission);
      stringBuilder.append(" ");
    }
    return stringBuilder.toString();
  }
}
