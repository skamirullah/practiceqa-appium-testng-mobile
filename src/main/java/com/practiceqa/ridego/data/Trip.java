package com.practiceqa.ridego.data;

/** Places and prices the tests rely on, kept in one place. All data is fictional. */
public final class Trip {
  public static final String OFFICE_QUERY = "Tech";
  public static final String OFFICE = "office";
  public static final String AIRPORT_QUERY = "air";
  public static final String AIRPORT = "airport";
  public static final String OFFICE_TO_AIRPORT_KM = "24.7 km";

  public static final String PROMO_CODE = "FIRSTRIDE";
  /** The promo takes 50% off the fare, up to this many rupees. */
  public static final int PROMO_MAX_DISCOUNT = 75;

  private Trip() {}
}
