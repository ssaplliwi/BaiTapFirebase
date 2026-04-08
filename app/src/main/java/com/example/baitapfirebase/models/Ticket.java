package com.example.baitapfirebase.models;

import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private String id;
    private String userId;
    private String userEmail;
    private String movieId;
    private String movieTitle;
    private String theaterId;
    private String theaterName;
    private String showtimeId;
    private String showDate;
    private String showTime;
    private int quantity;       // Số lượng vé
    private double totalPrice;
    private String bookedAt;   // Thời gian đặt (Timestamp)

    public Ticket() {}

    public Ticket(String id, String userId, String userEmail, String movieId, String movieTitle,
                  String theaterId, String theaterName, String showtimeId,
                  String showDate, String showTime, int quantity, double totalPrice, String bookedAt) {
        this.id = id;
        this.userId = userId;
        this.userEmail = userEmail;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.showtimeId = showtimeId;
        this.showDate = showDate;
        this.showTime = showTime;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.bookedAt = bookedAt;
    }

    // Chuyển sang Map để lưu lên Firebase
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", userId);
        map.put("userEmail", userEmail);
        map.put("movieId", movieId);
        map.put("movieTitle", movieTitle);
        map.put("theaterId", theaterId);
        map.put("theaterName", theaterName);
        map.put("showtimeId", showtimeId);
        map.put("showDate", showDate);
        map.put("showTime", showTime);
        map.put("quantity", quantity);
        map.put("totalPrice", totalPrice);
        map.put("bookedAt", bookedAt);
        return map;
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getUserEmail() { return userEmail; }
    public String getMovieId() { return movieId; }
    public String getMovieTitle() { return movieTitle; }
    public String getTheaterId() { return theaterId; }
    public String getTheaterName() { return theaterName; }
    public String getShowtimeId() { return showtimeId; }
    public String getShowDate() { return showDate; }
    public String getShowTime() { return showTime; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }
    public String getBookedAt() { return bookedAt; }
}
