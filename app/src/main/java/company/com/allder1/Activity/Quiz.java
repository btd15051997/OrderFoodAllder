package company.com.allder1.Activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import company.com.allder1.R;
import company.com.allder1.httpRequester.AsyncTaskCompleteListener;
import company.com.allder1.httpRequester.VollyRequester;
import company.com.allder1.miniGame.QuizDbHelper;
import company.com.allder1.model.Question;
import company.com.allder1.utils.Const;
import company.com.allder1.utils.PreferenceHelper;


public class Quiz extends AppCompatActivity  {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 15000;

//    private static final String KEY_SCORE = "keyScore";
//    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
//    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
//    private static final String KEY_ANSWERED = "keyAnswered";
//    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
//    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    String question  ;
    public  static String result ;
    String anw1 ;
    String anw2 ;
    String anw3 ;
    String anw4 ;



    private ArrayList<Question> questionList;

    private int questionCounter;
    private int questionCountTotal = 5;
    private Question currentQuestion;
    long backPressedTime = 0;
    int i = 0;
    String data;
    private int score;
    private boolean answered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        inti();

        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        Log.d("DATALOG",data);
        getData(data,i);

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestions();
        questionCountTotal = questionList.size();
//        Collections.shuffle(questionList);
        showNextQuestion();

//        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                i = i+1;
//                getData(data,i);
//                if (!answered) {
//                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
//                        checkAnswer();
//                    } else {
//                        Toast.makeText(Quiz.this, "Please select an answer", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    showNextQuestion();
//                }
//            }
//        });
//                }
//
//            }


    }
    private void inti() {
        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
//        buttonConfirmNext = findViewById(R.id.button_confirm_next);
        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();
    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal) {

            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(question);
            rb1.setText(anw1);
            rb2.setText(anw2);
            rb3.setText(anw3);
            rb4.setText(anw4);
            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);

//            buttonConfirmNext.setText("Confirm");

            answered = false;
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();


        } else {
            finishQuiz();
        }
    }
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                i = i + 1;
                getData(data,i);
                showNextQuestion();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormatted);
        if (timeLeftInMillis < 5000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
        if (timeLeftInMillis < 1000) {
            rb1.setTextColor(textColorDefaultRb);
            rb2.setTextColor(textColorDefaultRb);
            rb3.setTextColor(textColorDefaultRb);
            rb4.setTextColor(textColorDefaultRb);
            rbGroup.clearCheck();
        }
    }
    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel();
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());

        int answerNr = rbGroup.indexOfChild(rbSelected); // vị trí radio
        Log.d("ansWer",answerNr+"");
        if (answerNr == Integer.parseInt(result)) {
            Log.d("ansWer/Res",answerNr +"/"+result);
            score++;
            textViewScore.setText("Score:" + score);
        }
        showSolution();
    }
    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is correct");
                break;
        }
//        if (questionCounter < questionCountTotal) {
//            buttonConfirmNext.setText("Next");
//        } else {
//            buttonConfirmNext.setText("Finish");
//        }
    }

    private void finishQuiz() {

        Intent intentresult = new Intent();
        intentresult.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, intentresult);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void getData(String data,int y) {
        // manh 1
        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject object = new JSONObject(jsonArray.get(y).toString());
            question = object.getString("question");
            result = object.getString("result");
            anw1 = object.getString("answer_0");
            anw2 = object.getString("answer_1");
            anw3 = object.getString("answer_2");
            anw4 = object.getString("answer_3");

            textViewQuestion.setText(question);
            rb1.setText(anw1);
            rb2.setText(anw2);
            rb3.setText(anw3);
            rb4.setText(anw4);
            Log.d("DataMini1",  result);
            Log.d("Anwer", anw1 + anw2 + anw3 + anw4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ClickRadio(View view) {

        switch (view.getId()){
            case R.id.radio_button1:
                countDownTimer.cancel();
                updateCountDownText();
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        checkAnswer();
                        showNextQuestion();
                    } else {
                        Toast.makeText(Quiz.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                    i = i + 1;
                    getData(data,i);
                }

                break;
            case R.id.radio_button2:
                countDownTimer.cancel();
                updateCountDownText();
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        checkAnswer();
                        showNextQuestion();
                    } else {
                        Toast.makeText(Quiz.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                    i = i + 1;
                    getData(data,i);
                }
                break;
            case R.id.radio_button3:
                countDownTimer.cancel();
                updateCountDownText();
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        checkAnswer();
                        showNextQuestion();
                    } else {
                        Toast.makeText(Quiz.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                    i = i + 1;
                    getData(data,i);
                }
                break;
            case R.id.radio_button4:
                countDownTimer.cancel();
                updateCountDownText();
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        checkAnswer();
                        showNextQuestion();
                    } else {
                        Toast.makeText(Quiz.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                    i = i + 1;
                    getData(data,i);
                }
                break;

        }
    }
}
