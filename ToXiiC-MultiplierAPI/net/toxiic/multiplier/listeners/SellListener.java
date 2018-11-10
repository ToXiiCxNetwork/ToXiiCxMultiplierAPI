package net.toxiic.multiplier.listeners;

import org.bukkit.event.Listener;

import net.toxiic.multiplier.Multiplier;

public abstract class SellListener
  implements Listener
{
  public abstract String getName();
  
  protected Multiplier getPlugin()
  {
    return Multiplier.getInstance();
  }
}
