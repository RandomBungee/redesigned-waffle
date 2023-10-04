package de.jokileda.core.api.proxy.punish;

public interface Punish {

    void punishPlayer(PunishEntry punishEntry, PunishType punishType, boolean create);

    void removePunishByUnique(String uuid, PunishType punishType);

    PunishEntry findByUnique(String uuid);

    boolean isPunished(String uuid);
}
