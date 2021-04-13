package company.com.allder1.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataFood {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cuisine_category_id")
    @Expose
    private Integer cuisineCategoryId;
    @SerializedName("provider_id")
    @Expose
    private Integer providerId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("food_pictures")
    @Expose
    private String foodPictures;
    @SerializedName("is_approved")
    @Expose
    private Integer isApproved;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("comments")
    @Expose
    private Integer comments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCuisineCategoryId() {
        return cuisineCategoryId;
    }

    public void setCuisineCategoryId(Integer cuisineCategoryId) {
        this.cuisineCategoryId = cuisineCategoryId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFoodPictures() {
        return foodPictures;
    }

    public void setFoodPictures(String foodPictures) {
        this.foodPictures = foodPictures;
    }

    public Integer getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

}