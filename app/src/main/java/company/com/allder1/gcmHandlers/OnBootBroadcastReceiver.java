package company.com.allder1.gcmHandlers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.utils.AndyUtils;

/**
 * Created by user on 1/2/2017.
 */

public class OnBootBroadcastReceiver extends BroadcastReceiver implements AsyncTaskCompleteListener {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("mahi","on kill app received");
        Intent i = new Intent(".GCMRegisterHandler");
//        i.setClass(context, GCMRegisterHandler.class);
//        context.startService(i);
//        if (!AndyUtils.isNetworkAvailable(context)) {
//            return;
//        }
//        HashMap<String, String> map = new HashMap<>();
//        map.put(Const.Params.URL, Const.ServiceType.POST_AVAILABILITY_STATUS_URL);
//        map.put(Const.Params.ID, new PreferenceHelper(context).getUserId());
//        map.put(Const.Params.TOKEN, new PreferenceHelper(context).getSessionToken());
//        map.put(Const.Params.STATUS, "0");
//        map.put(Const.Params.FORCE_CLOSE,"1");
//        AndyUtils.appLog("asher", "AvailabilityMap" + map);
//        new VollyRequester(context, Const.POST, map, Const.ServiceCode.POST_AVAILABILITY_STATUS, this);
    }
    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        AndyUtils.appLog("asher", "AvailabilityResponse" + response);
    }

}