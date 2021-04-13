package company.com.allder1.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DetailOrder {

    @SerializedName("id")
    @Expose
    private Integer id;

    //====
    @SerializedName("chopsticks")
    @Expose
    private Integer chopsticks;

    @SerializedName("spoon")
    @Expose
    private Integer spoon;

    @SerializedName("fork")
    @Expose
    private Integer fork;

    @SerializedName("bowl")
    @Expose
    private Integer bowl;


    //====
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

    public Integer getChopsticks() {
        return chopsticks;
    }

    public void setChopsticks(Integer chopsticks) {
        this.chopsticks = chopsticks;
    }

    public Integer getSpoon() {
        return spoon;
    }

    public void setSpoon(Integer spoon) {
        this.spoon = spoon;
    }

    public Integer getFork() {
        return fork;
    }

    public void setFork(Integer fork) {
        this.fork = fork;
    }

    public Integer getBowl() {
        return bowl;
    }

    public void setBowl(Integer bowl) {
        this.bowl = bowl;
    }

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
    private String totalFoods;
    @SerializedName("order_detail")
    @Expose
    private ArrayList<DetailOrderFood> orderDetail = null;
    @SerializedName("order_number")
    @Expose
    private String order_number;
    @SerializedName("qr_code")
    @Expose
    private String qr_code;

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

    public String getTotalFoods() {
        return totalFoods;
    }

    public void setTotalFoods(String totalFoods) {
        this.totalFoods = totalFoods;
    }

    public ArrayList<DetailOrderFood> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(ArrayList<DetailOrderFood> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }
}