package net.toxiic.multiplier.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

public class Vault
{
  private static Object vaultEconomy = null;
  
  public static Economy getEconomy()
  {
    return vaultEconomy != null ? (Economy)vaultEconomy : null;
  }
  
  public static boolean hasVaultPermissions()
  {
    return (vaultEconomy != null) && (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault"));
  }
  
  public static void resetInstance()
  {
    vaultEconomy = null;
  }
  
  public static boolean setupEconomy()
  {
    try
    {
      if (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault"))
      {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
          vaultEconomy = economyProvider.getProvider();
        }
        return vaultEconomy != null;
      }
    }
    catch (Exception ex) {}
    return false;
  }
}
