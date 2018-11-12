package net.toxiic.multiplier;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public enum Lang
{
  COMMAND_USAGE("Command.Usage", "&6Usage: &c/%s"),  COMMAND_NO_PERMISSIONS("Command.No permissions", "&4You do not have access to that command."),  COMMAND_RELOADED("Command.Reload", "&6Reloaded ToXiiC-Multiplier's configuration."),  COMMAND_PLAYER_NOT_ONLINE("Command.Player not found", "&c%s is not online or does not exist!"),  COMMAND_NOT_VALID_NUMBER("Command.Invalid number", "&cPlease enter a valid number above 0 for the %s."),  COMMAND_SET("Command.Set", "&6Successfully set &c<player>&6's multiplier to <multiplier> lasting <duration>m."),  COMMAND_RESET("Command.Reset", "&6Successfully reset &c<player>&6's multiplier."),  TRANSACTION_RECEIVED("Transaction.Received", "&6You received an extra &c$<amount> &6for having a <multiplier>x multiplier."),  TRANSACTION_GAVE("Transaction.Gave", "&6You lost &c$<amount> &6because you have a multiplier below 1.");
  
  private static YamlConfiguration config = null;
  private static File configFile = null;
  private String key = "";
  private String defaultValue = "";
  
  private Lang(String key, String defValue)
  {
    this.key = key;
    this.defaultValue = defValue;
  }
  
  public String getMessage()
  {
    return replaceChatColours(getRawMessage());
  }
  
  public String getMessage(Object... format)
  {
    return replaceChatColours(String.format(getRawMessage(), format));
  }
  
  public String getRawMessage()
  {
    return config != null ? config.getString(this.key, this.defaultValue) : this.defaultValue;
  }
  
  public String getReplacedMessage(Object... objects)
  {
    String langMessage = getRawMessage();
    if (objects != null)
    {
      Object firstObject = null;
      for (int i = 0; i < objects.length; i++) {
        if (i % 2 == 0) {
          firstObject = objects[i];
        } else if ((firstObject != null) && (objects[i] != null)) {
          langMessage = langMessage.replace(firstObject.toString(), objects[i].toString());
        }
      }
    }
    return replaceChatColours(langMessage);
  }
  
  public static void sendMessage(CommandSender sender, Lang lang)
  {
    String strMessage = lang.getMessage();
    if (!strMessage.isEmpty())
    {
      List<String> messages = new ArrayList();
      if (strMessage.contains("\n"))
      {
        String[] messageSplit = strMessage.split("\n");
        for (String message : messageSplit) {
          messages.add(message);
        }
      }
      else
      {
        messages.add(strMessage);
      }
      String message;
      for (Iterator i$ = messages.iterator(); i$.hasNext(); sender.sendMessage(message)) {
        message = (String)i$.next();
      }
    }
  }
  
  public static void sendMessage(CommandSender sender, Lang lang, Object... objects)
  {
    String strMessage = lang.getMessage(objects);
    if (!strMessage.isEmpty())
    {
      List<String> messages = new ArrayList();
      if (strMessage.contains("\n"))
      {
        String[] messageSplit = strMessage.split("\n");
        for (String message : messageSplit) {
          messages.add(message);
        }
      }
      else
      {
        messages.add(strMessage);
      }
      String message;
      for (Iterator i$ = messages.iterator(); i$.hasNext(); sender.sendMessage(message)) {
        message = (String)i$.next();
      }
    }
  }
  
  public static void sendReplacedMessage(CommandSender sender, Lang lang, Object... objects)
  {
    String strMessage = lang.getReplacedMessage(objects);
    if (!strMessage.isEmpty())
    {
      List<String> messages = new ArrayList();
      if (strMessage.contains("\n"))
      {
        String[] messageSplit = strMessage.split("\n");
        for (String message : messageSplit) {
          messages.add(message);
        }
      }
      else
      {
        messages.add(strMessage);
      }
      String message;
      for (Iterator i$ = messages.iterator(); i$.hasNext(); sender.sendMessage(message)) {
        message = (String)i$.next();
      }
    }
  }
  
  public static void init(JavaPlugin plugin)
  {
    if (configFile == null) {
      configFile = new File(plugin.getDataFolder(), "messages.yml");
    }
    config = YamlConfiguration.loadConfiguration(configFile);
    for (Lang value : values()) {
      if (!config.isSet(value.key)) {
        config.set(value.key, value.defaultValue);
      }
    }
    try
    {
      config.save(configFile);
    }
    catch (Exception ex) {}
  }
  
  public static String getString(String path)
  {
    return config.getString(path);
  }
  
  public static String getString(String path, String defaultValue)
  {
    return config.getString(path, defaultValue);
  }
  
  public static String saveString(String path, String value)
  {
    if (!config.isSet(path))
    {
      config.set(path, value);
      try
      {
        config.save(configFile);
      }
      catch (Exception ex) {}
    }
    return config.isSet(path) ? config.getString(value) : value;
  }
  
  private static String replaceChatColours(String aString)
  {
    return aString != null ? ChatColor.translateAlternateColorCodes('&', aString) : "";
  }
}
