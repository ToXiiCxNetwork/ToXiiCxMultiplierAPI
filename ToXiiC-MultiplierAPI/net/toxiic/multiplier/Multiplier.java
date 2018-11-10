package net.toxiic.multiplier;

import net.toxiic.multiplier.listeners.BossShopListener;
import net.toxiic.multiplier.listeners.SellListener;
import net.toxiic.multiplier.utils.Utils;
import net.toxiic.multiplier.utils.Vault;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Multiplier
  extends JavaPlugin
  implements Listener
{
  private static Multiplier pluginInstance = null;
  private SellListener listenerBoss = null;
  private Map<UUID, MultiplierData> playerData = null;
  public Permission reloadPermission = new Permission("multiplier.command.reload");
  public Permission setPermission = new Permission("multiplier.command.set");
  
  public void onEnable()
  {
    pluginInstance = this;
    ConfigurationSerialization.registerClass(MultiplierData.class);
    Vault.setupEconomy();
    loadData();
    
    getCommand("multiplier").setExecutor(new CommandListener());
    
    getServer().getPluginManager().addPermission(this.reloadPermission);
    getServer().getPluginManager().addPermission(this.setPermission);
    try
    {
      if (getServer().getPluginManager().isPluginEnabled("BossShop"))
      {
        this.listenerBoss = new BossShopListener();
        getServer().getPluginManager().registerEvents(this.listenerBoss, this);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    getServer().getPluginManager().registerEvents(this, this);
    
    getServer().getScheduler().runTaskTimer(this, new Runnable()
    {
      public void run()
      {
        if (Multiplier.this.playerData != null)
        {
          List<UUID> uuidsToRemove = new ArrayList();
          for (Map.Entry<UUID, MultiplierData> dataEntry : Multiplier.this.playerData.entrySet()) {
            if ((dataEntry.getValue() != null) && 
              (System.currentTimeMillis() - ((MultiplierData)dataEntry.getValue()).getStartingTime() > ((MultiplierData)dataEntry.getValue()).getDuration() * 60.0D * 1000.0D))
            {
              uuidsToRemove.add(dataEntry.getKey());
              if (dataEntry.getKey() != null) {
                Multiplier.this.getConfig().set("Data." + ((UUID)dataEntry.getKey()).toString(), null);
              }
            }
          }
          UUID uuid;
          for (Iterator i$ = uuidsToRemove.iterator(); i$.hasNext(); Multiplier.this.playerData.remove(uuid)) {
            uuid = (UUID)i$.next();
          }
          if (!uuidsToRemove.isEmpty()) {
            Multiplier.this.saveConfig();
          }
        }
      }
    }, 0L, 5L);
  }
  
  public void onDisable()
  {
    getServer().getScheduler().cancelTasks(this);
    ConfigurationSerialization.unregisterClass(MultiplierData.class);
    
    getServer().getPluginManager().removePermission(this.reloadPermission);
    getServer().getPluginManager().removePermission(this.setPermission);
    
    this.listenerBoss = null;
    Vault.resetInstance();
    pluginInstance = null;
  }
  
  protected void loadData()
  {
    getConfig().options().header("ToXiiC Multiplier configuration");
    if ((!getConfig().contains("Data")) || (!getConfig().isConfigurationSection("Data")))
    {
      if (getConfig().contains("Data")) {
        getConfig().set("Data", null);
      }
      getConfig().createSection("Data");
    }
    getConfig().options().copyHeader(true);
    if (this.playerData == null) {
      this.playerData = new HashMap();
    } else {
      this.playerData.clear();
    }
    Map<String, Object> configData = getConfig().getConfigurationSection("Data").getValues(false);
    for (Map.Entry<String, Object> configDataEntry : configData.entrySet()) {
      try
      {
        Map<String, Object> serializedMap = (configDataEntry.getValue() instanceof Map) ? (Map)configDataEntry.getValue() : (configDataEntry.getValue() instanceof ConfigurationSection) ? ((ConfigurationSection)configDataEntry.getValue()).getValues(false) : null;
        if (serializedMap != null)
        {
          MultiplierData multiplierData = MultiplierData.deserialize(serializedMap);
          if (multiplierData != null)
          {
            if ((multiplierData.getMultiplier() != 1.0D) && (multiplierData.getMultiplier() > 0.0D))
            {
              if (Utils.isUUID((String)configDataEntry.getKey())) {
                this.playerData.put(UUID.fromString((String)configDataEntry.getKey()), multiplierData);
              } else {
                getConfig().set("Data." + (String)configDataEntry.getKey(), null);
              }
            }
            else {
              getConfig().set("Data." + (String)configDataEntry.getKey(), null);
            }
          }
          else {
            getLogger().warning("Could not register player '" + (String)configDataEntry.getKey() + "' because: The configuration data is invalid.");
          }
        }
        else
        {
          getLogger().warning("Could not register player '" + (String)configDataEntry.getKey() + "' because: The configuration value is not a valid configuration section.");
        }
      }
      catch (Exception ex)
      {
        getLogger().warning("Could not register player '" + (String)configDataEntry.getKey() + "' because: " + ex.getLocalizedMessage());
      }
    }
    saveConfig();
  }
  
  public double getMultiplier(Player player)
  {
    if ((player != null) && (this.playerData != null) && (this.playerData.get(player.getUniqueId()) != null))
    {
      double playerMultiplier = ((MultiplierData)this.playerData.get(player.getUniqueId())).getMultiplier();
      if (playerMultiplier != 1.0D) {
        return playerMultiplier;
      }
      this.playerData.remove(player.getUniqueId());
    }
    return 1.0D;
  }
  
  public SellListener getListenerBossShop()
  {
    return this.listenerBoss;
  }
  
  public boolean hasMultiplier(Player player)
  {
    return (player != null) && (this.playerData.get(player.getUniqueId()) != null);
  }
  
  public void setMultiplier(Player player, double multiplier, double duration)
  {
    if (player != null)
    {
      if (multiplier == 1.0D)
      {
        this.playerData.remove(player.getUniqueId());
        getConfig().set("Data." + player.getUniqueId().toString(), null);
      }
      else
      {
        MultiplierData playerData = new MultiplierData(multiplier, duration);
        this.playerData.put(player.getUniqueId(), playerData);
        getConfig().set("Data." + player.getUniqueId().toString(), playerData.serialize());
      }
      saveConfig();
    }
  }
  
  @EventHandler
  public void onPluginEnable(PluginEnableEvent event)
  {
    try
    {
      if ((this.listenerBoss == null) && (event.getPlugin().getName().equals("BossShop")))
      {
        this.listenerBoss = new BossShopListener();
        
        getServer().getPluginManager().registerEvents(this.listenerBoss, this);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  @EventHandler
  public void onPluginDisable(PluginDisableEvent event)
  {
    try
    {
      if ((this.listenerBoss != null) && (event.getPlugin().getName().equals("BossShop")))
      {
        HandlerList.unregisterAll(this.listenerBoss);
        this.listenerBoss = null;
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public static Multiplier getInstance()
  {
    return pluginInstance;
  }
}
