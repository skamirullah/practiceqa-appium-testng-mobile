package com.practiceqa.ridego.pages;

import io.qameta.allure.Step;

public class TripPage extends BasePage {
  /** A ride goes from searching to completed in about 11 seconds; allow for a slow device. */
  private static final long TRIP_SECONDS = 30;

  /** Waits for the ride to reach a stage. The app moves through the stages by itself. */
  @Step("Wait for the ride to be: {status}")
  public TripPage waitForStatus(String status) {
    waitForText(testId("ride-status"), status, TRIP_SECONDS);
    return this;
  }

  public String status() {
    return text(testId("ride-status"));
  }

  public String driverName() {
    return text(testId("driver-name"));
  }

  public String startOtp() {
    return text(testId("ride-otp"));
  }

  @Step("Rate the driver {stars} stars with a tip of ₹{tip}")
  public TripPage rateDriver(int stars, int tip) {
    click(testId("rate-driver-" + stars));
    click(testId("tip-" + tip));
    click(testId("submit-rating"));
    return this;
  }

  public String ratingConfirmation() {
    return text(testId("rating-saved"));
  }

  public int totalPaid() {
    return rupees(text(testId("trip-total")));
  }

  public String cancellationWarning() {
    click(testId("cancel-ride"));
    return text(testId("cancel-fee-warning"));
  }

  /** Finishes a cancellation whose dialog was opened by {@link #cancellationWarning()}. */
  @Step("Cancel the ride: {reason}")
  public TripPage confirmCancellation(String reason) {
    select(testId("cancel-reason"), reason);
    click(testId("confirm-cancel-ride"));
    return this;
  }

  public int cancellationFee() {
    return rupees(text(testId("cancel-fee")));
  }
}
