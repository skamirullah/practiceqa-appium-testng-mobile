package com.practiceqa.ridego.pages;

import io.qameta.allure.Step;

public class BookingPage extends BasePage {

  @Step("Open RideGo")
  public BookingPage open() {
    open("/apps/ride");
    visible(testId("pickup-input"));
    return this;
  }

  /** Types into the pickup box and taps the matching suggestion. */
  @Step("Set pickup to {placeId}")
  public BookingPage pickupFrom(String query, String placeId) {
    type(testId("pickup-input"), query);
    click(testId("pickup-option-" + placeId));
    return this;
  }

  @Step("Set drop to {placeId}")
  public BookingPage dropAt(String query, String placeId) {
    type(testId("drop-input"), query);
    click(testId("drop-option-" + placeId));
    return this;
  }

  public String distance() {
    return text(testId("trip-distance"));
  }

  public boolean asksForLocations() {
    return visible(testId("ride-hint")).isDisplayed();
  }

  /** @param ride bike, auto, mini, sedan or suv */
  @Step("Choose ride type: {ride}")
  public BookingPage chooseRide(String ride) {
    click(testId("ride-" + ride));
    return this;
  }

  public int fareFor(String ride) {
    return rupees(text(testId("fare-" + ride)));
  }

  @Step("Apply promo code {code}")
  public BookingPage applyPromo(String code) {
    type(testId("ride-promo"), code);
    click(testId("apply-ride-promo"));
    return this;
  }

  public String promoMessage() {
    return text(testId("ride-promo-message"));
  }

  public int payableFare() {
    return rupees(text(testId("fare-payable")));
  }

  public BookingPage payBy(String method) {
    select(testId("ride-payment"), method);
    return this;
  }

  /** Taps Book without expecting success; used by validation tests. */
  public BookingPage tapBook() {
    click(testId("book-ride"));
    return this;
  }

  public String error() {
    return text(testId("ride-error"));
  }

  @Step("Book the ride")
  public TripPage book() {
    tapBook();
    waitForUrl("/apps/ride/trip/");
    return new TripPage();
  }
}
