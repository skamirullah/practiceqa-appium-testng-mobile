package com.practiceqa.ridego.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

/**
 * Settings for a run. Each value is looked up as a system property first
 * (-Dplatform=android), then an environment variable (PLATFORM), then config.properties.
 */
public final class Config {
  private static final Properties FILE = load();

  private Config() {}

  private static Properties load() {
    Properties properties = new Properties();
    try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
      if (in != null) properties.load(in);
    } catch (IOException e) {
      throw new IllegalStateException("Could not read config.properties", e);
    }
    return properties;
  }

  private static String get(String key) {
    String fromSystem = System.getProperty(key);
    if (fromSystem != null && !fromSystem.isBlank()) return fromSystem;
    String fromEnv = System.getenv(key.toUpperCase().replace('.', '_'));
    if (fromEnv != null && !fromEnv.isBlank()) return fromEnv;
    return FILE.getProperty(key, "");
  }

  public static String baseUrl() {
    return get("base.url");
  }

  public static String platform() {
    return get("platform").toLowerCase();
  }

  public static String appiumUrl() {
    return get("appium.url");
  }

  public static String deviceName() {
    return get("device.name");
  }

  /** Empty when Appium should pick the only connected device. */
  public static String deviceUdid() {
    return get("device.udid");
  }

  /** Empty when any OS version will do. */
  public static String platformVersion() {
    return get("platform.version");
  }

  public static String emulationDevice() {
    return get("emulation.device");
  }

  public static boolean headless() {
    return Boolean.parseBoolean(get("headless"));
  }

  public static Duration timeout() {
    return Duration.ofSeconds(Long.parseLong(get("timeout")));
  }
}
