package net.toxiic.multiplier.listeners;

import net.milkbowl.vault.economy.Economy;
import net.toxiic.multiplier.Lang;
import net.toxiic.multiplier.Multiplier;
import net.toxiic.multiplier.utils.DebugManager;
import net.toxiic.multiplier.utils.Vault;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.enums.BSBuyType;
import org.black_ixx.bossshop.events.BSPlayerPurchasedEvent;
import org.bukkit.event.EventHandler;

public class BossShopListener
  extends SellListener
{
  @EventHandler
  public void onSold(BSPlayerPurchasedEvent event)
  {
    try
    {
      DebugManager.sendMessage("Run purchased event.");
      if (Vault.hasVaultPermissions())
      {
        DebugManager.sendMessage("Has vault.");
        if (getPlugin().hasMultiplier(event.getPlayer()))
        {
          DebugManager.sendMessage("Has multiplier.");
          double playerMultiplier = getPlugin().getMultiplier(event.getPlayer());
          DebugManager.sendMessage("Multiplier", Double.valueOf(playerMultiplier));
          if (playerMultiplier != 1.0D)
          {
            DebugManager.sendMessage("Buy type", event.getShopItem().getBuyType().name());
            if (event.getShopItem().getBuyType() == BSBuyType.Money)
            {
              Object objReward = event.getShopItem().getReward();
              if (objReward != null)
              {
                double reward = 0.0D;
                if ((objReward instanceof Integer)) {
                  reward = ((Integer)objReward).doubleValue();
                } else if ((objReward instanceof Double)) {
                  reward = ((Double)objReward).doubleValue();
                } else {
                  return;
                }
                DebugManager.sendMessage("Reward", Double.valueOf(reward));
                double newReward = reward * playerMultiplier;
                double extra = newReward - reward;
                DebugManager.sendMessage("Extra", Double.valueOf(extra));
                if (extra < 0.0D)
                {
                  Vault.getEconomy().withdrawPlayer(event.getPlayer(), extra);
                  Lang.sendReplacedMessage(event.getPlayer(), Lang.TRANSACTION_GAVE, new Object[] { "<amount>", String.format("%.2f", new Object[] { Double.valueOf(extra) }), "<multiplier>", String.format("%.1f", new Object[] { Double.valueOf(playerMultiplier) }) });
                }
                else if (extra > 0.0D)
                {
                  Vault.getEconomy().depositPlayer(event.getPlayer(), extra);
                  Lang.sendReplacedMessage(event.getPlayer(), Lang.TRANSACTION_RECEIVED, new Object[] { "<amount>", String.format("%.2f", new Object[] { Double.valueOf(extra) }), "<multiplier>", String.format("%.1f", new Object[] { Double.valueOf(playerMultiplier) }) });
                }
              }
            }
          }
        }
      }
      DebugManager.sendMessage(" ");
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public String getName()
  {
    return "BossShop";
  }
}
