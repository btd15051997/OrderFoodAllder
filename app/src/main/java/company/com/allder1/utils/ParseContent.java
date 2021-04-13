package company.com.allder1.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import company.com.allder1.model.Listfood;
import company.com.allder1.model.User;
import company.com.allder1.realmController.RealmController;
import io.realm.Realm;


public class ParseContent {
    private Activity activity;
    private Realm mRealm;
    private PreferenceHelper preferenceHelper;
    private final String KEY_SUCCESS = "success";
    private final String KEY_ERROR = "error";
    private final String KEY_ERROR_CODE = "error_code";
    public static final String IS_CANCELLED = "is_cancelled";

    public ParseContent(Activity activity) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
        preferenceHelper = new PreferenceHelper(activity);
    }


    public boolean isSuccessWithStoreId(String response) {
        if (TextUtils.isEmpty(response))
            return false;
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(KEY_SUCCESS) == true) {
                preferenceHelper.putUserId(jsonObject
                        .getString(Const.Params.ID));
                preferenceHelper.putSessionToken(jsonObject
                        .getString(Const.Params.TOKEN));
//                preferenceHelper.putPassword(jsonObject
//                        .getString(Const.Params.PASSWORD));
                preferenceHelper.putUser_name(jsonObject
                        .getString(Const.Params.FIRSTNAME));
                preferenceHelper.putlastname(jsonObject
                        .getString(Const.Params.LAST_NAME));
                preferenceHelper.putPhone(jsonObject
                        .getString(Const.Params.PHONE));
                preferenceHelper.putEmail(jsonObject
                        .optString(Const.Params.EMAIL));
                preferenceHelper.putPicture(jsonObject
                        .optString(Const.Params.PICTURE));
//                preferenceHelper.putLoginBy(jsonObject
//                        .getString(Const.Params.LOGIN_BY));
                preferenceHelper.putGeder(jsonObject
                        .getString(Const.Params.GENDER));
                if (!preferenceHelper.getLoginBy().equalsIgnoreCase(
                        Const.MANUAL)) {
                    preferenceHelper.putSocialId(jsonObject
                            .getString(Const.Params.SOCIAL_ID));
                }
                return true;
            } else {
                return false;

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public User parseUserAndStoreToDb(String response) {
        User user = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(KEY_SUCCESS)) {
                user = new User();
                mRealm = Realm.getInstance(activity);
                RealmController.with(activity).clearAll();
                user.setId(jsonObject.getInt(Const.Params.ID));
                user.setEmail(jsonObject.optString(Const.Params.EMAIL));
                user.setFname(jsonObject.getString(Const.Params.FIRSTNAME));
                user.setLname(jsonObject.getString(Const.Params.LAST_NAME));
                user.setProfileurl(jsonObject.getString(Const.Params.PICTURE));
                user.setPhone(jsonObject.getString(Const.Params.PHONE));
                if (jsonObject.has(Const.Params.CURRENCEY)) {
                    user.setCurrency(jsonObject.getString(Const.Params.CURRENCEY));
                }
                if (jsonObject.has(Const.Params.GENDER)) {
                    user.setGender(jsonObject.getString(Const.Params.GENDER));
                }
                if (jsonObject.has(Const.Params.COUNTRY)) {
                    user.setCountry(jsonObject.getString(Const.Params.COUNTRY));
                }
                if (jsonObject.has(Const.Params.CURRENCEY)) {
                    preferenceHelper.putCurrency(jsonObject.getString(Const.Params.CURRENCEY));
                }
                mRealm.beginTransaction();
//                mRealm.copyToRealm(user);
                mRealm.commitTransaction();


            } else {
                // AndyUtils.showToast(jsonObject.getString(KEY_ERROR),
                // activity);

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }

    public ArrayList<Listfood> parseListfoods(JSONArray response) {
        ArrayList<Listfood> listfoods = null;
        TypeToken<List<Listfood>> token = new TypeToken<List<Listfood>>() {
        };
        listfoods = new Gson().fromJson(response.toString(), token.getType());
        return listfoods;
    }
}
