package com.example.baitapfirebase.models;

import java.util.HashMap;
import java.util.Map;

public class Movie {
    private String id;
    private String title;
    private String description;
    private String posterUrl;
    private double rating;
    private String genre;       // Thể loại (Hành động, Tình cảm,...)
    private String duration;    // Thời lượng (vd: "120 phút")
    private double price;       // Giá vé
    private String imageName;   // Tên ảnh trong drawable (vd: "movie_avatar")

    // Constructor trống cho Firebase
    public Movie() {}

    public Movie(String id, String title, String description, String posterUrl,
                 double rating, String genre, String duration, double price, String imageName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.posterUrl = posterUrl;
        this.rating = rating;
        this.genre = genre;
        this.duration = duration;
        this.price = price;
        this.imageName = imageName;
    }

    // Chuyển sang Map để lưu lên Firebase
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("description", description);
        map.put("posterUrl", posterUrl);
        map.put("rating", rating);
        map.put("genre", genre);
        map.put("duration", duration);
        map.put("price", price);
        map.put("imageName", imageName);
        return map;
    }

    // Getters và Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }
}