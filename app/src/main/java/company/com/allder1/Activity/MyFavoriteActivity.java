package company.com.allder1.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.model.Question;
import company.com.allder1.utils.Const;

public class MyFavoriteActivity extends AppCompatActivity  {
    private static final int REQUEST_CODE_QUIZ = 1;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    private TextView textviewHighscore;
    private int highscore;
    int i = 0;
    Button buttonStartQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        textviewHighscore = findViewById(R.id.text_view_highscore);
        loadHighscore();
        buttonStartQuiz = findViewById(R.id.button_start_quiz);
        getData();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(Quiz.EXTRA_SCORE, 0);
                Log.d("score",score+"");
                updateHighscore(score);
            }
        }
    }
    private void loadHighscore() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        Log.d("HighScoreLoad",highscore+"");
        textviewHighscore.setText("Totalscore: " + highscore);
    }
    private void updateHighscore(int highscoreNew) {
        highscore += highscoreNew;
        // high score bỏ dấu +
        Log.d("HighScore",highscore+"");
        textviewHighscore.setText("Highscore: " + highscore);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }
    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(MyFavoriteActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Const.GET, "http://allder.qooservices.cf/managetmentFoodApi/minigame/one", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                Log.d("Api111", "getData: "+response);
                buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String array = String.valueOf(response);
                        Intent intent = new Intent(getApplicationContext(),Quiz.class);
                        intent.putExtra("data", array);
                        startActivityForResult(intent,REQUEST_CODE_QUIZ);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

//    @Override
//    public void onTaskCompleted(final String response, int serviceCode) {
//        Log.d("Api111", "getData: "+response);
//        switch (serviceCode) {
//            case 1111:
//                Log.d("Api111", "getData: "+response);
//                buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getApplicationContext(),Quiz.class);
//                        intent.putExtra("data",response);
//                        startActivityForResult(intent,REQUEST_CODE_QUIZ);
//                    }
//                });
//                break;
//
//        }
//    }
}
