package com.practiceqa.ridego.driver;

import com.practiceqa.ridego.config.Config;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Creates the session for the configured platform. Every platform returns a
 * WebDriver, so page objects and tests are the same on Android, iOS and emulation.
 */
public final class DriverFactory {

  private DriverFactory() {}

  public static WebDriver create() {
    return switch (Config.platform()) {
      case "android" -> android();
      case "ios" -> ios();
      case "emulation" -> emulation();
      default -> throw new IllegalArgumentException(
          "Unsupported platform: " + Config.platform() + " (use android, ios or emulation)");
    };
  }

  /** Chrome on an Android device or emulator. */
  private static WebDriver android() {
    UiAutomator2Options options = new UiAutomator2Options()
        .setDeviceName(Config.deviceName())
        .withBrowserName("Chrome")
        .setNewCommandTimeout(Duration.ofSeconds(120));
    // Skip Chrome's first-run and sign-in screens so the test starts on the page.
    options.setCapability("goog:chromeOptions", Map.of("args", new String[] {"--no-first-run", "--disable-fre"}));
    // Let Appium download a chromedriver that matches the Chrome on the device.
    options.setCapability("appium:chromedriverAutodownload", true);
    if (!Config.deviceUdid().isBlank()) options.setUdid(Config.deviceUdid());
    if (!Config.platformVersion().isBlank()) options.setPlatformVersion(Config.platformVersion());
    return new AndroidDriver(appiumServer(), options);
  }

  /** Safari on an iPhone or simulator. */
  private static WebDriver ios() {
    XCUITestOptions options = new XCUITestOptions()
        .setDeviceName(Config.deviceName())
        .withBrowserName("Safari")
        .setNewCommandTimeout(Duration.ofSeconds(120));
    if (!Config.deviceUdid().isBlank()) options.setUdid(Config.deviceUdid());
    if (!Config.platformVersion().isBlank()) options.setPlatformVersion(Config.platformVersion());
    return new IOSDriver(appiumServer(), options);
  }

  /**
   * Desktop Chrome with a phone's screen size, pixel ratio, touch events and user
   * agent. It is not a real device, but it runs anywhere, so it is the fast
   * check used on every commit.
   */
  private static WebDriver emulation() {
    ChromeOptions options = new ChromeOptions();
    options.setExperimentalOption("mobileEmulation", Map.of("deviceName", Config.emulationDevice()));
    if (Config.headless()) options.addArguments("--headless=new");
    return new ChromeDriver(options);
  }

  private static URL appiumServer() {
    try {
      return URI.create(Config.appiumUrl()).toURL();
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("appium.url is not a valid URL: " + Config.appiumUrl(), e);
    }
  }
}
