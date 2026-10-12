package com.practiceqa.ridego.pages;

import com.practiceqa.ridego.config.Config;
import com.practiceqa.ridego.driver.DriverManager;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Shared behaviour for every page object. Every action waits for its element
 * first, so tests never call Thread.sleep and pages never expose raw locators.
 */
public abstract class BasePage {
  protected final WebDriver driver = DriverManager.driver();
  protected final WebDriverWait wait = new WebDriverWait(driver, Config.timeout());

  /** The app marks every interactive element with data-testid. */
  protected static By testId(String id) {
    return By.cssSelector("[data-testid='" + id + "']");
  }

  protected void open(String path) {
    driver.get(Config.baseUrl() + path);
  }

  protected WebElement visible(By locator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  /**
   * Waits until the element can be tapped and scrolls it to the middle of the
   * screen first. On a phone the sticky header and the on-screen keyboard cover
   * the edges, which is where the driver would otherwise scroll it to.
   */
  protected WebElement clickable(By locator) {
    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'})", element);
    return element;
  }

  protected void click(By locator) {
    clickable(locator).click();
  }

  protected void type(By locator, String text) {
    WebElement field = clickable(locator);
    field.clear();
    field.sendKeys(text);
  }

  protected void select(By locator, String value) {
    new Select(clickable(locator)).selectByValue(value);
  }

  protected void check(By locator) {
    WebElement box = clickable(locator);
    if (!box.isSelected()) box.click();
  }

  protected String text(By locator) {
    return visible(locator).getText();
  }

  /** Waits for at least {@code count} matches, then returns all of them. */
  protected List<WebElement> atLeast(By locator, int count) {
    return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, count - 1));
  }

  protected boolean isPresent(By locator) {
    return !driver.findElements(locator).isEmpty();
  }

  protected void waitForUrl(String fragment) {
    wait.until(ExpectedConditions.urlContains(fragment));
  }

  protected void waitForText(By locator, String text) {
    wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
  }

  /** Waits up to {@code seconds} for text that the app changes by itself on a timer. */
  protected void waitForText(By locator, String text, long seconds) {
    new WebDriverWait(driver, java.time.Duration.ofSeconds(seconds))
        .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
  }

  /** "₹432" → 432 */
  protected static int rupees(String money) {
    return Integer.parseInt(money.replaceAll("[^0-9]", ""));
  }
}
