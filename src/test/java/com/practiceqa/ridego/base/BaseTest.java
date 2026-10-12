package com.practiceqa.ridego.base;

import com.practiceqa.ridego.data.Trip;
import com.practiceqa.ridego.driver.DriverManager;
import com.practiceqa.ridego.pages.BookingPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/** Starts a fresh browser session on the device for every test and ends it afterwards, pass or fail. */
public abstract class BaseTest {

  @BeforeMethod(alwaysRun = true)
  public void startSession() {
    DriverManager.start();
  }

  @AfterMethod(alwaysRun = true)
  public void endSession() {
    DriverManager.quit();
  }

  protected BookingPage bookingPage() {
    return new BookingPage().open();
  }

  /** The route most tests use: office to airport. */
  protected BookingPage officeToAirport() {
    return bookingPage().pickupFrom(Trip.OFFICE_QUERY, Trip.OFFICE).dropAt(Trip.AIRPORT_QUERY, Trip.AIRPORT);
  }
}
