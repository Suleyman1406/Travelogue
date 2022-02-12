package com.okada.travelogue.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.okada.travelogue.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextInputLayout forgotPasswordEmailLayout;
    private TextInputEditText forgotPasswordEmailText;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        findingViews();
        firebaseAuth = FirebaseAuth.getInstance();
        setTouchListeners();

    }
    @SuppressLint("ClickableViewAccessibility")
    public void setTouchListeners(){
        forgotPasswordEmailText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                forgotPasswordEmailLayout.setErrorEnabled(false);
                return false;
            }
        });
    }
    public void findingViews(){
        forgotPasswordEmailText = findViewById(R.id.forgot_password_email_text);
        forgotPasswordEmailLayout = findViewById(R.id.forgot_password_email_layout);
    }
    public void resetPassword(View view) {

        String email = forgotPasswordEmailText.getText().toString();

        if (!TextUtils.isEmpty(email)) {
            firebaseAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.isSuccessful()){
                                    if (task.getResult()!=null && task.getResult().getSignInMethods()!=null){
                                        if (task.getResult().getSignInMethods().isEmpty()){
                                            forgotPasswordEmailLayout.setError("Can't find e-mail");
                                        }else {
                                            Toast.makeText(ForgotPasswordActivity.this,"We sent a link to "+email,Toast.LENGTH_SHORT).show();
                                            firebaseAuth.sendPasswordResetEmail(email);
                                            Intent toHome = new Intent(ForgotPasswordActivity.this,HomeActivity.class);
                                            finish();
                                            startActivity(toHome);
                                        }
                                    }

                                }else if (task.getException()!=null){
                                    Exception e=task.getException();
                                    if (e instanceof FirebaseAuthException) {
                                      if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_INVALID_EMAIL")) {
                                           forgotPasswordEmailLayout.setError("Invalid email");
                                        } else {
                                          Toast.makeText(ForgotPasswordActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                      }

                                    }
                                }
                        }
                    });

        } else
            forgotPasswordEmailLayout.setError("E-mail can't be empty.");
    }
}