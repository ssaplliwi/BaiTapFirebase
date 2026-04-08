package com.example.baitapfirebase.models;

public class Showtime {
    private String id;
    private String movieId;
    private String theaterId;
    private String time;       // Giờ chiếu: "14:00", "16:30", "19:00"
    private String date;       // Ngày chiếu: "2026-04-10"
    private int availableSeats;

    public Showtime() {}

    public Showtime(String id, String movieId, String theaterId, String time, String date, int availableSeats) {
        this.id = id;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.time = time;
        this.date = date;
        this.availableSeats = availableSeats;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getTheaterId() { return theaterId; }
    public void setTheaterId(String theaterId) { this.theaterId = theaterId; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}
