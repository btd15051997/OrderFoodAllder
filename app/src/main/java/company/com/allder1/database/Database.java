package company.com.allder1.database;

import android.content.Context;
import android.content.SharedPreferences;

public class Database {
    private SharedPreferences homefood;
    private final String LISTCATEGORY = "listcategory";
    private final String LISTRECOMMENDED = "listrecommended";
    private final String LISTSTORENEARBY = "nearbystore";
    private final String STORE = "store";
    private Context context;

    public Database(Context context) {
        homefood = context.getSharedPreferences("category",
                Context.MODE_PRIVATE);
        this.context = context;
    }

    public void putListcategory(String listcategory) {
        SharedPreferences.Editor edit = homefood.edit();
        edit.putString(LISTCATEGORY, listcategory);
        edit.commit();
    }

    public String getListcategory() {
        return homefood.getString(LISTCATEGORY, "");
    }

    public void putlistrecommended(String listrecommended) {
        SharedPreferences.Editor edit = homefood.edit();
        edit.putString(LISTRECOMMENDED, listrecommended);
        edit.commit();
    }

    public String getlistrecommended() {
        return homefood.getString(LISTRECOMMENDED, "");
    }

    public void putnearbystore(String nearbystore) {
        SharedPreferences.Editor edit = homefood.edit();
        edit.putString(LISTSTORENEARBY, nearbystore);
        edit.commit();
    }

    public String getnearbystore() {
        return homefood.getString(LISTSTORENEARBY, "");
    }

    public void putStore(String store) {
        SharedPreferences.Editor edit = homefood.edit();
        edit.putString(STORE, store);
        edit.commit();
    }

    public String getStore() {
        return homefood.getString(STORE, "");
    }
}
