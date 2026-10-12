package com.practiceqa.ridego.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.practiceqa.ridego.base.BaseTest;
import com.practiceqa.ridego.data.Trip;
import com.practiceqa.ridego.pages.BookingPage;
import com.practiceqa.ridego.pages.TripPage;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Feature("Booking a ride")
public class BookingTests extends BaseTest {

  @Test(groups = "smoke", description = "A rider books a cab, takes the trip, and rates the driver")
  public void booksARideAndCompletesTheTrip() {
    BookingPage booking = officeToAirport().chooseRide("mini").applyPromo(Trip.PROMO_CODE).payBy("upi");
    int payable = booking.payableFare();
    int tip = 20;

    TripPage trip = booking.book().waitForStatus("Driver assigned");
    assertThat(trip.driverName()).isNotBlank();
    assertThat(trip.startOtp()).matches("\\d{4}");

    trip.waitForStatus("Driver arrived").waitForStatus("On trip").waitForStatus("Completed").rateDriver(5, tip);

    assertThat(trip.ratingConfirmation()).contains("5★");
    assertThat(trip.totalPaid()).isEqualTo(payable + tip);
  }

  @Test(description = "Pickup and drop are needed before booking")
  public void routeIsRequired() {
    BookingPage booking = bookingPage().tapBook();

    assertThat(booking.error()).isEqualTo("Choose different pickup and drop locations");
  }

  @Test(description = "A ride type is needed before booking")
  public void rideTypeIsRequired() {
    BookingPage booking = officeToAirport().tapBook();

    assertThat(booking.error()).isEqualTo("Choose a ride type");
  }

  @Test(description = "Cancelling before the driver arrives is free")
  public void cancellingEarlyIsFree() {
    TripPage trip = officeToAirport().chooseRide("auto").book();

    String warning = trip.cancellationWarning();
    trip.confirmCancellation("Changed my plans");

    assertThat(warning).isEqualTo("Free cancellation before your driver arrives.");
    assertThat(trip.status()).isEqualTo("Cancelled");
    assertThat(trip.cancellationFee()).isZero();
  }

  @Test(description = "Cancelling after the driver has arrived costs a fee")
  public void cancellingLateCostsAFee() {
    TripPage trip = officeToAirport().chooseRide("auto").book().waitForStatus("Driver arrived");

    String warning = trip.cancellationWarning();
    trip.confirmCancellation("Booked by mistake");

    assertThat(warning).contains("cancellation fee applies");
    assertThat(trip.cancellationFee()).isPositive();
  }
}
