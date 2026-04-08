package com.example.baitapfirebase.models;

public class Theater {
    private String id;
    private String name;
    private String address;
    private String imageName; // ảnh rạp trong drawable

    public Theater() {}

    public Theater(String id, String name, String address, String imageName) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.imageName = imageName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }
}
