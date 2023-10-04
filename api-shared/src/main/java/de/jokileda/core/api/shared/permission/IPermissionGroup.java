package de.jokileda.core.api.shared.permission;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public interface IPermissionGroup {

  void createOpUpdateGroup(String name, String prefix, int priority, List<String> permission);

  void deleteGroup(String name);

  String getGroupPrefix(String name);

  String getTabTeam(String name);

  List<String> getGroupPermissions(List<String> groups);

  List<String> allGroups();

  boolean groupExist(String name);

}
