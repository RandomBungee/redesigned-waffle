package de.jokileda.core.api.spigot.inventory;

import de.jokileda.core.api.spigot.AbstractSpigotPlugin;
import de.jokileda.core.api.spigot.config.SpigotConfigManager;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class SpigotInventory implements InventoryHolder, Listener {

  private final Player player;
  private final Map<Integer, Inventory> pages;
  private final AbstractSpigotPlugin plugin;
  private final SpigotConfigManager config;
  private final Inventory inventory;
  private int currentPage;

  public SpigotInventory(Player player, AbstractSpigotPlugin plugin, SpigotConfigManager config) {
    this.player = player;
    this.pages = new HashMap<>();
    this.plugin = plugin;
    this.config = config;
    this.currentPage = 0;
    this.inventory = Bukkit.createInventory(this, config.getInt("Inventory.size", 54), config.getString("Inventory.title", "Inv"));
  }

  @Override
  public Inventory getInventory() {
    return pages.get(currentPage);
  }

  public void addItem(String path, int slot, int page, Consumer<InventoryClickEvent> clickHandler) {
    if (!pages.containsKey(currentPage)) {
      pages.put(currentPage, inventory);
    }

    Inventory targetInventory = pages.get(page);
    if (targetInventory.getItem(slot) == null) {
      targetInventory.setItem(slot, config.getItemStack("Inventory.item." + path));

      if (clickHandler != null) {
        setItemClickConsumer(slot, clickHandler, page);
      }
    }
  }

  public void autoFill() {
    int currentPageSize = getPageSize();
    if (!pages.containsKey(currentPage)) {
      pages.put(currentPage, inventory);
    }

    Inventory currentPageInventory = pages.get(currentPage);
    for (int i = 0; i < currentPageSize * 9; i++) {
      if (currentPageInventory.getItem(i) == null) {
        currentPageInventory.setItem(i, config.getItemStack("Inventory.fillItems"));
      }
    }
  }

  public void openInventory() {
    if (pages.containsKey(currentPage)) {
      player.openInventory(pages.get(currentPage));
    }
  }

  public void nextPage() {
    currentPage++;
    openInventory();
  }

  public void prevPage() {
    currentPage--;
    if (currentPage < 0) {
      currentPage = 0;
    }
    openInventory();
  }

  public int getPageSize() {
    return 6;
  }

  private void setItemClickConsumer(int slot, Consumer<InventoryClickEvent> clickHandler, int page) {
    Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
      @EventHandler
      public void onItemClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getInventory().equals(getInventory()) && event.getSlot() == slot) {
          clickHandler.accept(event);
        }
      }
    }, plugin);
  }
}
