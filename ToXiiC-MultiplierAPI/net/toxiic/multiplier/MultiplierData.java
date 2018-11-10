package net.toxiic.multiplier;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import net.toxiic.multiplier.utils.Utils;

public class MultiplierData
  implements ConfigurationSerializable
{
  private double multiplier = 1.5D;
  private long startingTime = 0L;
  private double duration = 5.0D;
  
  public MultiplierData(double multiplier, double duration)
  {
    this.multiplier = (multiplier <= 0.0D ? 1.0D : multiplier);
    this.startingTime = System.currentTimeMillis();
    this.duration = (duration > 0.0D ? duration : 5.0D);
  }
  
  public double getDuration()
  {
    return this.duration;
  }
  
  public double getMultiplier()
  {
    return this.multiplier;
  }
  
  public long getStartingTime()
  {
    return this.startingTime;
  }
  
  public MultiplierData setMultiplier(double multiplier)
  {
    this.multiplier = multiplier;
    return this;
  }
  
  public MultiplierData setTime(long startingTime)
  {
    this.startingTime = startingTime;
    return this;
  }
  
  public Map<String, Object> serialize()
  {
    Map<String, Object> serializedMultiplier = new HashMap();
    serializedMultiplier.put("Multiplier", Double.valueOf(this.multiplier));
    serializedMultiplier.put("Start", Long.valueOf(this.startingTime));
    serializedMultiplier.put("Duration", Double.valueOf(this.duration));
    return serializedMultiplier;
  }
  
  public static MultiplierData deserialize(Map<String, Object> serializedMultiplier)
  {
    if ((serializedMultiplier != null) && (serializedMultiplier.containsKey("Multiplier")))
    {
      Object objMultiplier = serializedMultiplier.get("Multiplier");
      if (objMultiplier == null) {
        objMultiplier = Double.valueOf(1.5D);
      }
      double multiplier = Utils.isDouble(objMultiplier.toString()) ? Double.parseDouble(objMultiplier.toString()) : 1.0D;
      long startTime = System.currentTimeMillis();
      if (serializedMultiplier.get("Start") != null) {
        startTime = Utils.isLong(serializedMultiplier.get("Start").toString()) ? Long.parseLong(serializedMultiplier.get("Start").toString()) : System.currentTimeMillis();
      }
      Object objDuration = serializedMultiplier.get("Duration");
      if (objDuration == null) {
        objDuration = Double.valueOf(5.0D);
      }
      double duration = Utils.isDouble(objDuration.toString()) ? Double.parseDouble(objDuration.toString()) : 5.0D;
      return new MultiplierData(multiplier, duration).setTime(startTime);
    }
    return null;
  }
}
