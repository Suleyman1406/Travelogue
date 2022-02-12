package com.okada.travelogue.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.okada.travelogue.R;

public class SignUpPage2Activity extends AppCompatActivity {

    private TextInputEditText signUpPasswordText, signUpConfirmPasswordText;
    private TextInputLayout signUpPasswordLayout, signUpConfirmPasswordLayout;
    private String name, surname, email;
    private ImageView imageView2;
    private TextView signInStartTextView, signInStartTextView2;
    private Button signInBtn;
    private LinearLayout linearLayout;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page2);
        findingViews();
        getIntentExtras();
        firebaseAuth = FirebaseAuth.getInstance();
        setTouchListeners();

    }
    @SuppressLint("ClickableViewAccessibility")
    public void setTouchListeners(){
        signUpPasswordText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                signUpPasswordLayout.setErrorEnabled(false);
                signUpConfirmPasswordLayout.setErrorEnabled(false);
                return false;
            }
        });
        signUpPasswordLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                signUpPasswordLayout.setErrorEnabled(false);
                signUpConfirmPasswordLayout.setErrorEnabled(false);
                return false;
            }
        });
        signUpConfirmPasswordText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                signUpConfirmPasswordLayout.setErrorEnabled(false);
                signUpPasswordLayout.setErrorEnabled(false);
                return false;
            }
        });
    }
    public void getIntentExtras(){
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        surname = intent.getStringExtra("surname");
        email = intent.getStringExtra("email");
    }
    public void findingViews(){
        signUpPasswordText = findViewById(R.id.sign_up_password_edit_text);
        signUpConfirmPasswordText = findViewById(R.id.sign_up_confirm_password_edit_text);
        signUpPasswordLayout = findViewById(R.id.sign_up_password_layout);
        signUpConfirmPasswordLayout = findViewById(R.id.sign_up_confirm_password_layout);
        imageView2 = findViewById(R.id.imageView2);
        signInStartTextView = findViewById(R.id.signInStartTextView);
        signInStartTextView2 = findViewById(R.id.signInStartTextView2);
        progressBar=findViewById(R.id.sign_up2_progress_bar);
        signInBtn = findViewById(R.id.signInBtn1);
        linearLayout = findViewById(R.id.linearLayout);
    }
    public void createAccount(View view) {
        if (!isConnected(SignUpPage2Activity.this)){
            showDialog(SignUpPage2Activity.this);
        }else {
            progressBar.setVisibility(View.VISIBLE);
            String password = signUpPasswordText.getText().toString();
            String confirmPassword = signUpConfirmPasswordText.getText().toString();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
            }
            if (TextUtils.isEmpty(password)) {
                progressBar.setVisibility(View.INVISIBLE);
                signUpPasswordLayout.setError("Password can't be empty");
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                progressBar.setVisibility(View.INVISIBLE);
                signUpConfirmPasswordLayout.setError("Confirm password can't be empty");
            }
            if (!TextUtils.isEmpty(password) & !TextUtils.isEmpty(confirmPassword)) {
                if (!confirmPassword.equals(password)) {
                    progressBar.setVisibility(View.INVISIBLE);
                    signUpPasswordLayout.setError("Passwords are not same");
                    signUpConfirmPasswordLayout.setError("Passwords are not same");
                } else if (password.contains(" ")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    signUpPasswordLayout.setError("Passwords can't contain white space");
                    signUpConfirmPasswordLayout.setError("Passwords can't contain white space");
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);

                            if (e instanceof FirebaseAuthException) {
                                if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_WEAK_PASSWORD")) {
                                    signUpConfirmPasswordLayout.setError("Weak password");
                                    signUpPasswordLayout.setError("Weak password");
                                } else {
                                    Toast.makeText(SignUpPage2Activity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Intent toHomeActivity = new Intent(SignUpPage2Activity.this, HomeActivity.class);
                            Toast.makeText(SignUpPage2Activity.this,"User successfully created",Toast.LENGTH_SHORT).show();
                            startActivity(toHomeActivity);
                            Animatoo.animateSwipeRight(SignUpPage2Activity.this);
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }
    }
    public boolean isConnected(Context context){
        ConnectivityManager manager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile=manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi!=null&&wifi.isConnected())||(mobile!=null&&mobile.isConnected())){
            return true;
        }else {
            return false;
        }
    }
    public void showDialog(Context context){
        AlertDialog.Builder builder=new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage("We can't find internet connection. Please connect to internet")
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(context,HomeActivity.class));
                        Animatoo.animateSlideRight(context);
                        finish();
                    }
                });
        builder.show();
    }
    public void toSignIn(View view) {
        Intent intent = new Intent(SignUpPage2Activity.this, SignInActivity.class);
        Pair[] pairs = new Pair[5];
        pairs[0] = new Pair<View, String>(imageView2, "sign_in_start_logo_image");
        pairs[1] = new Pair<View, String>(signInStartTextView, "sign_in_start_text_view");
        pairs[2] = new Pair<View, String>(signInStartTextView2, "sign_up_to_continue");
        pairs[3] = new Pair<View, String>(signInBtn, "sign_up_btn");
        pairs[4] = new Pair<View, String>(linearLayout, "sign_up_already_have_layout");
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SignUpPage2Activity.this, pairs);
        startActivity(intent, activityOptions.toBundle());
        finish();
    }

}