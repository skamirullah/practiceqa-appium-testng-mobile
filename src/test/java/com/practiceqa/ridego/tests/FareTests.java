package com.practiceqa.ridego.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.practiceqa.ridego.base.BaseTest;
import com.practiceqa.ridego.data.Trip;
import com.practiceqa.ridego.pages.BookingPage;
import io.qameta.allure.Feature;
import java.util.List;
import org.testng.annotations.Test;

@Feature("Fares and promo codes")
public class FareTests extends BaseTest {

  @Test(description = "Fares appear only after pickup and drop are chosen")
  public void faresNeedARoute() {
    BookingPage booking = bookingPage();
    assertThat(booking.asksForLocations()).isTrue();

    booking.pickupFrom(Trip.OFFICE_QUERY, Trip.OFFICE).dropAt(Trip.AIRPORT_QUERY, Trip.AIRPORT);

    assertThat(booking.distance()).contains(Trip.OFFICE_TO_AIRPORT_KM);
    assertThat(booking.fareFor("mini")).isPositive();
  }

  @Test(description = "A bigger vehicle costs more for the same route")
  public void biggerVehiclesCostMore() {
    BookingPage booking = officeToAirport();

    List<Integer> fares = List.of("bike", "auto", "mini", "sedan", "suv").stream().map(booking::fareFor).toList();

    assertThat(fares).isSorted().doesNotHaveDuplicates();
  }

  @Test(description = "The promo takes half off the fare, up to its maximum")
  public void promoHalvesTheFareUpToItsMaximum() {
    BookingPage booking = officeToAirport().chooseRide("mini");
    int fare = booking.fareFor("mini");
    int discount = Math.min(Math.round(fare * 0.5f), Trip.PROMO_MAX_DISCOUNT);

    booking.applyPromo(Trip.PROMO_CODE);

    assertThat(booking.promoMessage()).isEqualTo("50% off up to ₹75 applied");
    assertThat(booking.payableFare()).isEqualTo(fare - discount);
  }

  @Test(description = "An unknown promo code is rejected")
  public void unknownPromoIsRejected() {
    BookingPage booking = officeToAirport().chooseRide("mini").applyPromo("FREERIDE");

    assertThat(booking.promoMessage()).isEqualTo("Invalid promo code");
  }
}
