package net.toxiic.multiplier;

import org.bukkit.entity.Player;

public class MultiplierAPI
{
  public static double getMultiplier(Player player)
  {
    return Multiplier.getInstance().getMultiplier(player);
  }
  
  public static boolean hasMultiplier(Player player)
  {
    return Multiplier.getInstance().hasMultiplier(player);
  }
  
  public static void removeMultiplier(Player player)
  {
    if (player != null) {
      Multiplier.getInstance().setMultiplier(player, 1.0D, 0.0D);
    }
  }
  
  public static void setMultiplier(Player player, double multiplier)
  {
    setMultiplier(player, multiplier, 5.0D);
  }
  
  public static void setMultiplier(Player player, double multiplier, double duration)
  {
    if ((multiplier > 0.0D) && (duration > 0.0D)) {
      Multiplier.getInstance().setMultiplier(player, multiplier, duration);
    }
  }
  
  public static void reloadConfig()
  {
    Lang.init(Multiplier.getInstance());
    reloadData();
  }
  
  public static void reloadData()
  {
    Multiplier.getInstance().reloadConfig();
    Multiplier.getInstance().loadData();
  }
}
