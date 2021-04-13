package company.com.allder1.Adapter;

public class Cartfood {
    int id;
    int quatity;

    public Cartfood(int id_food, int amount) {
        this.id = id_food;
        this.quatity = amount;
    }

    public int getId_food() {
        return id;
    }

    public void setId_food(int id_food) {
        this.id = id_food;
    }

    public int getAmount() {
        return quatity;
    }

    public void setAmount(int amount) {
        this.quatity = amount;
    }
}
