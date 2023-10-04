package de.jokileda.core.api.shared.permission;

import java.util.List;

public interface IPermissionUser {

  void createPermissionUser(String uuid,
      String group,
      List<String> permissions);

  void updateUser(String uuid,
      String group,
      long groupTime,
      boolean overrideGroup);

  void updateUser(String uuid, String permission);

  List<String> getUserLists(String uuid, String column);

  boolean userExist(String uuid);
}