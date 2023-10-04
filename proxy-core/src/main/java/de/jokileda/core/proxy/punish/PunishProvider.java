package de.jokileda.core.proxy.punish;

import de.jokileda.core.api.proxy.AbstractProxyPlugin;
import de.jokileda.core.api.proxy.punish.Punish;
import de.jokileda.core.api.proxy.punish.PunishEntry;
import de.jokileda.core.api.proxy.punish.PunishType;
import de.jokileda.shared.SharedLoader;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
public class PunishProvider implements Punish {

    private final SharedLoader sharedLoader;
    private final AbstractProxyPlugin abstractProxyPlugin;

    @Override
    public void punishPlayer(PunishEntry punishEntry, PunishType punishType, boolean create) {
        PunishProviderExtension punishProviderExtension = new PunishProviderExtension(abstractProxyPlugin);
        long end;
        if (punishEntry.getTime() == -1L) {
            end = -1L;
        } else {
            long current = System.currentTimeMillis();
            long millis = punishEntry.getTime() * 1000;
            end = current + millis;
        }
        if (create) {
            sharedLoader.iSql.preparedStatementWithObjects("INSERT INTO punish_players (unique_id,reason,author,time,punish_type) VALUES (?,?,?,?,?)",
                    punishEntry.getUuid(),
                    punishEntry.getReason(),
                    punishEntry.getAuthor(),
                    end,
                    punishType.toString());
            punishProviderExtension.punishPlayer(UUID.fromString(punishEntry.getUuid()),
                    punishEntry.getReason(),
                    end,
                    punishType);
            return;
        }
        sharedLoader.iSql.preparedStatementWithObjects("UPDATE punish_players SET reason = ?, author = ?, time = ? WHERE unique_id = ? AND punish_type = ?",
                punishEntry.getReason(),
                punishEntry.getAuthor(),
                end,
                punishEntry.getUuid(),
                punishType.toString());
    }

    @Override
    public void removePunishByUnique(String uuid, PunishType punishType) {
        sharedLoader.iSql.preparedStatementWithObjects("DELETE FROM punish_players WHERE unique_id = ? AND punish_type = ?",
                uuid,
                punishType.toString());
    }

    @Override
    public PunishEntry findByUnique(String uuid) {
        CompletableFuture<ResultSet> future = sharedLoader.iSql.executeQueryResultSet(
                "SELECT * FROM punish_players WHERE unique_id = ?", uuid);
        String reason = "";
        String author = "";
        long time = 0;
        PunishType punishType = null;
        try {
            ResultSet resultSet = future.get();
            if(resultSet.next()) {
                reason = resultSet.getString("reason");
                author = resultSet.getString("author");
                time = resultSet.getLong("time");
                punishType = PunishType.valueOf(resultSet.getString("punish_type"));
            }
        } catch (InterruptedException | ExecutionException | SQLException e) {
            throw new RuntimeException(e);
        }
        return PunishEntry.
                newBuilder().
                setUuid(uuid).
                setReason(reason).
                setAuthor(author).
                setTime(time).
                setPunishType(punishType).
                build();
    }

    @Override
    public boolean isPunished(String uuid) {
        CompletableFuture<Boolean> future = sharedLoader.iSql.executeQuery(
                "SELECT * FROM punish_players WHERE unique_id = ?", uuid);
        boolean exist;
        try {
            exist = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return exist;
    }
}
