package company.com.allder1.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Storefood {

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("data_provider")
@Expose
private List<DataProvider> dataProvider = null;
@SerializedName("data_foods")
@Expose
private List<DataFood> dataFoods = null;

public Boolean getSuccess() {
return success;
}

public void setSuccess(Boolean success) {
this.success = success;
}

public List<DataProvider> getDataProvider() {
return dataProvider;
}

public void setDataProvider(List<DataProvider> dataProvider) {
this.dataProvider = dataProvider;
}

public List<DataFood> getDataFoods() {
return dataFoods;
}

public void setDataFoods(List<DataFood> dataFoods) {
this.dataFoods = dataFoods;
}

}