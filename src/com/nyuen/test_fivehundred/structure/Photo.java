package com.nyuen.test_fivehundred.structure;

public class Photo {
    private String id;
    private String name;
    private String description;
    private int times_viewed;
    private double rating;
    private String created_at;
    private int category;
    private boolean privacy;
    private int width;
    private int height;
    private int votes_count;
    private int favorites_count;
    private int comments_count;
    private boolean nsfw;
    private String image_url;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getTimes_viewed() {
        return times_viewed;
    }
    public void setTimes_viewed(int times_viewed) {
        this.times_viewed = times_viewed;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public String getCreated_at() {
        return created_at;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public int getCategory() {
        return category;
    }
    public void setCategory(int category) {
        this.category = category;
    }
    public boolean isPrivacy() {
        return privacy;
    }
    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getVotes_count() {
        return votes_count;
    }
    public void setVotes_count(int votes_count) {
        this.votes_count = votes_count;
    }
    public int getFavorites_count() {
        return favorites_count;
    }
    public void setFavorites_count(int favorites_count) {
        this.favorites_count = favorites_count;
    }
    public int getComments_count() {
        return comments_count;
    }
    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }
    public boolean isNsfw() {
        return nsfw;
    }
    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }
    public String getImage_url() {
        return image_url;
    }
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
    
    
}
