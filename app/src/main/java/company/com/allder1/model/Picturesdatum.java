package company.com.allder1.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Picturesdatum {

@SerializedName("picture")
@Expose
private String picture;

public String getPicture() {
return picture;
}

public void setPicture(String picture) {
this.picture = picture;
}

}