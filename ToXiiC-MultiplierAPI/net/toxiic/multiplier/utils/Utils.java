package net.toxiic.multiplier.utils;

import java.util.UUID;

public class Utils
{
  public static boolean isDouble(String aString)
  {
    try
    {
      Double.parseDouble(aString);
      return true;
    }
    catch (Exception ex) {}
    return false;
  }
  
  public static boolean isLong(String aString)
  {
    try
    {
      Long.parseLong(aString);
      return true;
    }
    catch (Exception ex) {}
    return false;
  }
  
  public static boolean isUUID(String aString)
  {
    try
    {
      return UUID.fromString(aString) != null;
    }
    catch (Exception ex) {}
    return false;
  }
}
