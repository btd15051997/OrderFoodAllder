package company.com.allder1.model;

public class AddCartLoco {
    int idfood;
    String  amount;

    public AddCartLoco(int idfood, String amount) {
        this.idfood = idfood;
        this.amount = amount;
    }

    public int getIdfood() {
        return idfood;
    }

    public void setIdfood(int idfood) {
        this.idfood = idfood;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
