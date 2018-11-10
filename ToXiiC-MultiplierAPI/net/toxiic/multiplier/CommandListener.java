package net.toxiic.multiplier;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import net.toxiic.multiplier.utils.DebugManager;
import net.toxiic.multiplier.utils.Utils;

public class CommandListener
  implements CommandExecutor
{
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (cmd.getName().equals("multiplier"))
    {
      if (args.length > 0)
      {
        if (args[0].equalsIgnoreCase("debug"))
        {
          if ((sender instanceof Player))
          {
            if ((sender.isOp()) || (getPlugin().getDescription().getAuthors().contains(sender.getName())))
            {
              Player player = (Player)sender;
              if (DebugManager.toggleDebugger(player.getUniqueId())) {
                player.sendMessage(ChatColor.GOLD + "You are now debugging ToXiiC Multiplier.");
              } else {
                player.sendMessage(ChatColor.RED + "You are no longer debugging ToXiiC Multiplier.");
              }
            }
            else
            {
              Lang.sendMessage(sender, Lang.COMMAND_NO_PERMISSIONS, new Object[] { cmd.getName() + " debug" });
            }
          }
          else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use that command.");
          }
        }
        else if (args[0].equalsIgnoreCase("reload"))
        {
          if (sender.hasPermission(getPlugin().reloadPermission))
          {
            if (args.length == 1)
            {
              MultiplierAPI.reloadConfig();
              Lang.sendMessage(sender, Lang.COMMAND_RELOADED);
            }
            else
            {
              sendHelp(sender, cmd);
            }
          }
          else {
            Lang.sendMessage(sender, Lang.COMMAND_NO_PERMISSIONS, new Object[] { cmd.getName() + " reload" });
          }
        }
        else if (args[0].equalsIgnoreCase("reset"))
        {
          if (sender.hasPermission(getPlugin().setPermission))
          {
            boolean sentMessage;
            if (args.length == 2)
            {
              List<Player> playerList = new ArrayList();
              if (args[1].equalsIgnoreCase("*")) {
                playerList = new ArrayList(sender.getServer().getOnlinePlayers());
              } else {
                playerList.add(sender.getServer().getPlayer(args[1]));
              }
              sentMessage = false;
              for (Player targetPlayer : playerList)
              {
                if ((targetPlayer != null) && (targetPlayer.isOnline()))
                {
                  if (getPlugin().hasMultiplier(targetPlayer)) {
                    getPlugin().setMultiplier(targetPlayer, 1.0D, 0.0D);
                  }
                  if (!sentMessage) {
                    Lang.sendReplacedMessage(sender, Lang.COMMAND_RESET, new Object[] { "<player>", args[1].equalsIgnoreCase("*") ? "everyone" : targetPlayer.getName() });
                  }
                }
                else if (!sentMessage)
                {
                  Lang.sendMessage(sender, Lang.COMMAND_PLAYER_NOT_ONLINE, new Object[] { args[1] });
                }
                sentMessage = true;
              }
            }
            else
            {
              sendHelp(sender, cmd);
            }
          }
          else
          {
            Lang.sendMessage(sender, Lang.COMMAND_NO_PERMISSIONS, new Object[] { cmd.getName() + " reset" });
          }
        }
        else if (args[0].equalsIgnoreCase("set"))
        {
          if (sender.hasPermission(getPlugin().setPermission))
          {
            boolean sentMessage;
            if ((args.length == 3) || (args.length == 4))
            {
              List<Player> playerList = new ArrayList();
              if (args[1].equalsIgnoreCase("*")) {
                playerList = new ArrayList(sender.getServer().getOnlinePlayers());
              } else {
                playerList.add(sender.getServer().getPlayer(args[1]));
              }
              sentMessage = false;
              for (Player targetPlayer : playerList)
              {
                if ((targetPlayer != null) && (targetPlayer.isOnline()))
                {
                  if (Utils.isDouble(args[2]))
                  {
                    double multiplier = Double.parseDouble(args[2]);
                    if (multiplier > 0.0D)
                    {
                      double duration = 5.0D;
                      if (args.length == 4) {
                        if ((Utils.isDouble(args[3])) || (args[3].equalsIgnoreCase("*")))
                        {
                          duration = args[3].equalsIgnoreCase("*") ? Double.MAX_VALUE : Double.parseDouble(args[3]);
                          if (duration <= 0.0D)
                          {
                            Lang.sendMessage(sender, Lang.COMMAND_NOT_VALID_NUMBER, new Object[] { "duration" });
                            return true;
                          }
                        }
                        else
                        {
                          Lang.sendMessage(sender, Lang.COMMAND_NOT_VALID_NUMBER, new Object[] { "duration" });
                          return true;
                        }
                      }
                      getPlugin().setMultiplier(targetPlayer, multiplier, duration);
                      if (!sentMessage) {
                        if (getPlugin().hasMultiplier(targetPlayer)) {
                          Lang.sendReplacedMessage(sender, Lang.COMMAND_SET, new Object[] { "<player>", args[1].equalsIgnoreCase("*") ? "everyone" : targetPlayer.getName(), "<multiplier>", Double.valueOf(multiplier), "<duration>", Double.valueOf(duration) });
                        } else {
                          Lang.sendReplacedMessage(sender, Lang.COMMAND_RESET, new Object[] { "<player>", args[1].equalsIgnoreCase("*") ? "everyone" : targetPlayer.getName() });
                        }
                      }
                    }
                    else if (!sentMessage)
                    {
                      Lang.sendMessage(sender, Lang.COMMAND_NOT_VALID_NUMBER, new Object[] { "multiplier" });
                    }
                  }
                  else if (!sentMessage)
                  {
                    Lang.sendMessage(sender, Lang.COMMAND_NOT_VALID_NUMBER, new Object[] { "multiplier" });
                  }
                }
                else if (!sentMessage) {
                  Lang.sendMessage(sender, Lang.COMMAND_PLAYER_NOT_ONLINE, new Object[] { args[1] });
                }
                sentMessage = true;
              }
            }
            else
            {
              sendHelp(sender, cmd);
            }
          }
          else
          {
            Lang.sendMessage(sender, Lang.COMMAND_NO_PERMISSIONS, new Object[] { cmd.getName() + " set" });
          }
        }
        else {
          sendHelp(sender, cmd);
        }
      }
      else {
        sendHelp(sender, cmd);
      }
      return true;
    }
    return false;
  }
  
  public void sendHelp(CommandSender sender, Command cmd)
  {
    if (sender != null)
    {
      sender.sendMessage(ChatColor.AQUA + "ToXiiC Multiplier");
      Lang.sendMessage(sender, Lang.COMMAND_USAGE, new Object[] { cmd != null ? cmd.getUsage().replaceFirst("/", "").replaceFirst("<command>", cmd.getName()) : "null" });
    }
  }
  
  protected Multiplier getPlugin()
  {
    return Multiplier.getInstance();
  }
}
