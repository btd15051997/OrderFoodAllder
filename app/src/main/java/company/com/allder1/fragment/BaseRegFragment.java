package company.com.allder1.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;


import company.com.allder1.Activity.SenOtpActivity;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;

/**
 * Created by user on 8/29/2016.
 */
public class BaseRegFragment extends Fragment implements
        View.OnClickListener,AsyncTaskCompleteListener {
    SenOtpActivity activity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        activity = (SenOtpActivity) getActivity();


    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

    }

    @Override
    public void onClick(View v) {


    }
}
