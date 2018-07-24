package com.indian.states.capitals.indianstates;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.indian.states.capitals.indianstates.Questions.category;


public class QuizMainActivity extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 1000;
    final static long TIMEOUT = 20000;
    int progressValue = 0;

    CountDownTimer countDownTimer;

    int index = 0, score = 0, thisQuestion = 0, totalQuestions, correctAnswer;

    private ProgressBar progressBar;

    Button choiceABtn, choiceBBtn, choiceCBtn, choiceDBtn;
    TextView txtScore, txtQuestionNum,txtTime, questionText;

    //firebase for High Score
    private DatabaseReference mDatabase;
    private  Integer mScore = 0;

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


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseDatabase.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mDatabase.child("HighScore").child(category);


    }
    private void showQuestion(int index) {

        if(index < totalQuestions) {
            txtTime.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            thisQuestion++;
            txtQuestionNum.setText((thisQuestion + "/" + totalQuestions));
            progressBar.setProgress(0);
            progressValue = 0;

            questionText.setText(Questions.questionList.get(index).getQuestion());

            choiceABtn.setBackground(getResources().getDrawable(R.drawable.background_white_border));
            choiceBBtn.setBackground(getResources().getDrawable(R.drawable.background_white_border));
            choiceCBtn.setBackground(getResources().getDrawable(R.drawable.background_white_border));
            choiceDBtn.setBackground(getResources().getDrawable(R.drawable.background_white_border));

            choiceABtn.setEnabled(true);
            choiceBBtn.setEnabled(true);
            choiceCBtn.setEnabled(true);
            choiceDBtn.setEnabled(true);

            choiceABtn.setText(Questions.questionList.get(index).getChoice1().trim());
            choiceBBtn.setText(Questions.questionList.get(index).getChoice2().trim());
            choiceCBtn.setText(Questions.questionList.get(index).getChoice3().trim());
            choiceDBtn.setText(Questions.questionList.get(index).getChoice4().trim());

            countDownTimer.start();
            //Start timer
        } else{
            //final question

            checkIfHighScored();
            Intent intent = new Intent(QuizMainActivity.this,HighScoreActivity.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE", score);
            dataSend.putInt("CORRECT", correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();

           // startActivity(intent);
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
                txtTime.setText(String.format("%d", (int) (millisUntilFinished / 1000)));
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
        Button selectedButton = (Button)v;
        String selected = selectedButton.getText().toString();
        String correct = Questions.questionList.get(index).getAnswer().trim();

        countDownTimer.cancel();
        txtTime.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        choiceABtn.setEnabled(false);
        choiceBBtn.setEnabled(false);
        choiceCBtn.setEnabled(false);
        choiceDBtn.setEnabled(false);

        if(correct.equals(selected)) {
            //Correct answer chosen
            score += 10;
            selectedButton.setBackground(getResources().getDrawable(R.drawable.green_border));
            correctAnswer++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showQuestion(++index);
                }
            },3000);
        } else {
            //Wrong answer chosen
            selectedButton.setBackground(getResources().getDrawable(R.drawable.red_border));

            if(choiceABtn.getText().toString().trim().equals(correct)) {
                choiceABtn.setBackground(getResources().getDrawable(R.drawable.green_border));
            }else if(choiceBBtn.getText().toString().trim().equals(correct)) {
                choiceBBtn.setBackground(getResources().getDrawable(R.drawable.green_border));
            }else if(choiceCBtn.getText().toString().trim().equals(correct)) {
                choiceCBtn.setBackground(getResources().getDrawable(R.drawable.green_border));
            }else if(choiceDBtn.getText().toString().trim().equals(correct)) {
                choiceDBtn.setBackground(getResources().getDrawable(R.drawable.green_border));
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showQuestion(++index);
                }
            },2000);
        }

        txtScore.setText(String.format("%s%d", getString(R.string.score), score));//display score


    }

    public void setHighScore(){

        if(score > mScore) {
            mDatabase.child("HighScore").child(category).child("Score").setValue(score).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });

        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizMainActivity.this);
            builder.setMessage("Your score:"+score)
                    .setCancelable(false)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(QuizMainActivity.this,HighScoreActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("Explore States and UTs", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(QuizMainActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();


                }
            });
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Indian States and Capitals Quiz");
            alert.show();
        }
    }

    public void checkIfHighScored() {


        mDatabase.child("HighScore").child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Score")) {
                    mScore = dataSnapshot.child("Score").getValue(Integer.class);
                }
                setHighScore();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();
    }
}
