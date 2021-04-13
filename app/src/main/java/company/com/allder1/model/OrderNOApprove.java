package company.com.allder1.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderNOApprove {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("type_person_order")
    @Expose
    private String typePersonOrder;
    @SerializedName("total_money")
    @Expose
    private Integer totalMoney;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("is_done")
    @Expose
    private Integer isDone;
    @SerializedName("is_received")
    @Expose
    private Integer isReceived;
    @SerializedName("order_for_provider_id")
    @Expose
    private Integer orderForProviderId;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("total_foods")
    @Expose
    private Integer totalFoods;
    @SerializedName("name_store")
    @Expose
    private String nameStore;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTypePersonOrder() {
        return typePersonOrder;
    }

    public void setTypePersonOrder(String typePersonOrder) {
        this.typePersonOrder = typePersonOrder;
    }

    public Integer getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIsDone() {
        return isDone;
    }

    public void setIsDone(Integer isDone) {
        this.isDone = isDone;
    }

    public Integer getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(Integer isReceived) {
        this.isReceived = isReceived;
    }

    public Integer getOrderForProviderId() {
        return orderForProviderId;
    }

    public void setOrderForProviderId(Integer orderForProviderId) {
        this.orderForProviderId = orderForProviderId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getTotalFoods() {
        return totalFoods;
    }

    public void setTotalFoods(Integer totalFoods) {
        this.totalFoods = totalFoods;
    }

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }
}