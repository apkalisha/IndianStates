package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizMainActivity extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 2000;
    final static long TIMEOUT = 10000; //10 Sec
    int progressValue = 0;

    CountDownTimer countDownTimer;

    int index = 0, score = 0, thisQuestion = 0, totalQuestions, correctAnswer;

    private ProgressBar progressBar;

    Button choiceABtn, choiceBBtn, choiceCBtn, choiceDBtn;
    TextView txtScore, txtQuestionNum,txtTime, questionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quiz_main);

        progressBar = findViewById(R.id.quiz_progress_time);
        txtScore = findViewById(R.id.txt_score);
        txtQuestionNum = findViewById(R.id.question_num);
        txtTime = findViewById(R.id.quiz_timer);
        questionText = findViewById(R.id.question);

        choiceABtn = findViewById(R.id.choiceA);
        choiceBBtn = findViewById(R.id.choiceB);
        choiceCBtn = findViewById(R.id.choiceC);
        choiceDBtn = findViewById(R.id.choiceD);

        choiceABtn.setOnClickListener(this);
        choiceBBtn.setOnClickListener(this);
        choiceCBtn.setOnClickListener(this);
        choiceDBtn.setOnClickListener(this);


    }
    private void showQuestion(int index) {

        if(index < totalQuestions) {

            thisQuestion++;
            txtQuestionNum.setText((thisQuestion + "/" + totalQuestions));
            progressBar.setProgress(0);
            progressValue = 0;

            questionText.setText(Questions.questionList.get(index).getQuestion());


            choiceABtn.setText(Questions.questionList.get(index).getChoice1());
            choiceBBtn.setText(Questions.questionList.get(index).getChoice2());
            choiceCBtn.setText(Questions.questionList.get(index).getChoice3());
            choiceDBtn.setText(Questions.questionList.get(index).getChoice4());

            countDownTimer.start();
            //Start timer
        } else{
            //final question
            Intent intent = new Intent(QuizMainActivity.this,HighScoreActivity.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL", totalQuestions);
            dataSend.putInt("CORRECT", correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestions = 10;
        countDownTimer = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressValue);
                //txtTime.setText((int) (millisUntilFinished/1000));
                progressValue++;

            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                showQuestion(++index);

            }
        };
        showQuestion(index);
    }
    @Override
    public void onClick(View v) {

        String selected = v.toString();

        countDownTimer.cancel();
        if(Questions.questionList.get(index).getAnswer().equals(selected)) { //still have questions in the list
            //Correct answer chosen
            v.setBackgroundColor(getResources().getColor(R.color.green));
            score += 10;
            correctAnswer++;
            showQuestion(++index);
        } else {
            //Wrong answer chosen
            v.setBackgroundColor(getResources().getColor(R.color.red));
            showQuestion(++index);
        }

        txtScore.setText(String.format("%s%d", getString(R.string.score), score));

    }
}
