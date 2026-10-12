package com.practiceqa.ridego.listeners;

import com.practiceqa.ridego.driver.DriverManager;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;

/** Attaches a screenshot and the page URL to the Allure report when a test fails. */
public class TestListener implements ITestListener {

  @Override
  public void onTestFailure(ITestResult result) {
    if (!DriverManager.isRunning()) return;
    byte[] screenshot = ((TakesScreenshot) DriverManager.driver()).getScreenshotAs(OutputType.BYTES);
    Allure.addAttachment("Screenshot at failure", "image/png", new ByteArrayInputStream(screenshot), "png");
    Allure.addAttachment("URL at failure", DriverManager.driver().getCurrentUrl());
  }
}
