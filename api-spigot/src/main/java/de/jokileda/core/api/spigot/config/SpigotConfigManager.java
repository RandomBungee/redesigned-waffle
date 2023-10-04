package de.jokileda.core.api.spigot.config;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpigotConfigManager {

  private final Config config;
  private static Field field;

  static {
    try {
      final Class craftMetaSkull = Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + ".inventory.CraftMetaSkull");
      field = craftMetaSkull.getDeclaredField("profile");
      field.setAccessible(true);
    } catch (final ReflectiveOperationException e) {
      e.printStackTrace();
    }
  }

  public SpigotConfigManager(String plugin, String fileName) {
    config = new Config(plugin, fileName);
  }

  public SpigotConfigManager(String plugin, Class<?> clazz) {
    config = new Config(plugin, clazz.getSimpleName().toLowerCase());
  }

  public Location getLocation(String path) {
    Location defaultLocation = new Location(Bukkit.getWorld("world"), 0, 75, 0);
    return config.get(path, defaultLocation);
  }

  public String getString(String path, String defaultValue, String... placeholder) {
    Map<String, String> placeholderMap = new HashMap<>();
    if (placeholder.length % 2 != 0) {
      throw new IllegalArgumentException("Placeholders must be provided in key-value pairs.");
    }
    for (int i = 0; i < placeholder.length; i += 2) {
      placeholderMap.put(placeholder[i], placeholder[i + 1]);
    }
    return getString(path, defaultValue, placeholderMap);
  }

  public String getString(String path, String defaultValue, Map<String, String> placeHolders) {
    String value = config.get(path, defaultValue);
    if(placeHolders == null)
      return config.get(path, defaultValue).replaceAll("%n", System.lineSeparator()).replaceAll(";", ":");
    for(Map.Entry<String, String> placeHolder : placeHolders.entrySet()) {
      value = value.replace(placeHolder.getKey(), placeHolder.getValue());
    }
    return value.replaceAll(";", ":").replaceAll("%n", System.lineSeparator());
  }

  public Integer getInt(String path, int defaultValue) {
    return config.get(path, defaultValue);
  }

  public Float getFloat(String path, float defaultValue) {
    return config.get(path, defaultValue);
  }

  public Long getLong(String path, long defaultValue) {
    return config.get(path, defaultValue);
  }

  public Boolean getBoolean(String path, boolean defaultValue) {
    return config.get(path, defaultValue);
  }

  public List<String> getStringList(String path, List<String> defaultValue) {
    return config.get(path, defaultValue);
  }

  public List<Integer> getIntegerList(String path, List<Integer> defaultValue) {
    return config.get(path, defaultValue);
  }

  public ItemStack getItemStack(String path) {
    String defaultDisplayName = "Default Item " + path;

    String displayName = getString(path + ".displayName", defaultDisplayName);
    List<String> lore = getStringList(path + ".lore", Arrays.asList("default1", "default2"));
    List<String> itemFlag = getStringList(path + ".flag", Arrays.asList("default1", "default2"));
    //int data = getInt(path + ".data", 0);
    boolean glow = getBoolean(path + ".glow", false);
    Material material = Material.valueOf(getString(path + ".material", "DIRT"));
    int amount = getInt(path + ".amount", 4);
    Map<Enchantment, Integer> enchantmentList = getEnchantmentList(path + ".enchants");

    ItemStack itemStack = new ItemStack(material, amount);

    if(!enchantmentList.isEmpty())
      itemStack.addUnsafeEnchantments(enchantmentList);

    ItemMeta itemMeta = itemStack.getItemMeta();

    if(itemMeta instanceof LeatherArmorMeta) {
      Color color = Color.fromRGB(getInt(path + ".custom.color", 0));
      ((LeatherArmorMeta) itemMeta).setColor(color);
    }

    if(itemMeta instanceof BookMeta) {
      BookMeta bookMeta = (BookMeta) itemMeta;
      String author = getString(path + ".custom.author", "RandomBungee");
      String title = getString(path + ".custom.title", "Titel");
      List<String> pages = getStringList(path + ".custom.pages", Arrays.asList("default1", "default2"));
      bookMeta.setAuthor(author);
      bookMeta.setTitle(title);
      bookMeta.setPages(pages);
    }

    if(itemMeta instanceof PotionMeta potionMeta) {
      List<PotionEffect> potionEffectList = getPotionEffectList(
          path + ".custom.potionEffect");
      potionEffectList.forEach(s -> potionMeta.addCustomEffect(s, true));
    }

    if(itemMeta instanceof BannerMeta bannerMeta) {
      bannerMeta.setPatterns(getBannerPatternList(path + ".custom.patterns"));
    }

    if(!itemFlag.containsAll(Arrays.asList("default1", "default2")))
      itemFlag.forEach(s -> itemMeta.addItemFlags(ItemFlag.valueOf(s)));

    if(!lore.containsAll(Arrays.asList("default1", "default2")))
      itemMeta.setLore(lore);

    if(itemMeta instanceof SkullMeta) {
      String skullMeta = getString(path + ".custom.skullMeta", "RandomBungee");
      if(skullMeta.length() > 16) {
        setSkullTexture(itemStack, skullMeta);
      } else {
        setSkullAndName(itemStack, skullMeta);
      }
    }

    itemMeta.setDisplayName(displayName);
    itemStack.setItemMeta(itemMeta);
    return itemStack;

  }

  private Map<Enchantment, Integer> getEnchantmentList(String path) {
    List<String> enchantments = getStringList(path, Arrays.asList("default1", "default2"));
    Map<Enchantment, Integer> map = new HashMap<>();
    if(!enchantments.containsAll(Arrays.asList("default1", "default2"))) {
      enchantments.forEach(s -> {
        try {
          String[] split = s.split(" ");
          Enchantment enchantment = Enchantment.getByName(split[0]);
          int i = Integer.parseInt(split[1]);
          if (i != -1 && enchantment != null) {
            map.put(enchantment, i);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }
    return map;
  }

  private List<PotionEffect> getPotionEffectList(String path) {
    List<String> rawPotionEffectList = getStringList(path, Arrays.asList("default1", "default2"));
    List<PotionEffect> potionEffects = new ArrayList<>();
    rawPotionEffectList.forEach(s -> {
      try {
        String[] split = s.split(" ");
        PotionEffectType effectType = PotionEffectType.getByName(split[0]);
        int duration = split.length >= 2 ? Integer.parseInt(split[1]) : 60 * 20;
        int amplifier = split.length >= 3 ? Integer.parseInt(split[2]) : 1;
        boolean ambient = split.length < 4 || Boolean.parseBoolean(split[3]);
        boolean particles = split.length < 5 || Boolean.parseBoolean(split[4]);
        potionEffects.add(new PotionEffect(effectType, duration, amplifier, ambient, particles));
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    return potionEffects;
  }

  private List<Pattern> getBannerPatternList(String path) {
    List<String> rawPatternList = getStringList(path, Arrays.asList("default1", "default2"));
    List<Pattern> patterns = new ArrayList<>();
    if(!rawPatternList.containsAll(Arrays.asList("default1", "default2"))) {
      rawPatternList.forEach(s -> {
        try {
          String[] split = s.split(" ");
          DyeColor dyeColor = DyeColor.valueOf(split[1].toUpperCase());
          PatternType patternType = PatternType.getByIdentifier(split[0]);
          if (patternType == null) patternType = PatternType.valueOf(split[0].toUpperCase());
          patterns.add(new Pattern(dyeColor, patternType));
        } catch (Exception ex) {
          System.out.println("The String " + s + " is not an valid option for banner!");
        }
      });
    }
    return patterns;
  }

  private void setSkullTexture(ItemStack itemStack, String textureHash) {
    SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
    GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
    gameProfile.getProperties().put("textures", new Property("textures", textureHash));
    try {
      field.set(itemMeta, gameProfile);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    itemStack.setItemMeta(itemMeta);
  }

  private void setSkullAndName(ItemStack itemStack, String name) {
    itemStack.setType(Material.PLAYER_HEAD);
    itemStack.setDurability((short) 3);
    SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
    skullMeta.setOwner(name);
    itemStack.setItemMeta(skullMeta);
  }
}
