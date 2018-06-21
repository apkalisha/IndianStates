package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity{

    EditText resetPasswordEmail;
    Button goBack, reset;
    private FirebaseAuth mAuth;
    ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPasswordEmail =(EditText)findViewById(R.id.et_email_reset);
        goBack=(Button)findViewById(R.id.btn_back);
        reset =(Button)findViewById(R.id.btn_reset);
        loginProgress =(ProgressBar)findViewById(R.id.login_progress);

        mAuth = FirebaseAuth.getInstance();


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail = resetPasswordEmail.getText().toString().trim();

                if(useremail.equals("")){
                    Toast.makeText(ResetPasswordActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    resetPasswordEmail.requestFocus();
                }else{
                    loginProgress.setVisibility(View.VISIBLE);
                    mAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this, "Reset email sent successfully", Toast.LENGTH_LONG).show();
                                finish();
                                loginProgress.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                            }else{
                                Toast.makeText(ResetPasswordActivity.this, "Please Check your registered email", Toast.LENGTH_LONG).show();
                                loginProgress.setVisibility(View.INVISIBLE);
                                resetPasswordEmail.requestFocus();
                            }
                        }
                    });
                }
            }
        });

    }
}
