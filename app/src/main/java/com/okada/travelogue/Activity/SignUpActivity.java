package com.okada.travelogue.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.okada.travelogue.R;

public class SignUpActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextInputEditText signUpNameText, signUpSurnameText, signUpEmailText;
    private TextInputLayout signUpNameLayout, signUpSurnameLayout, signUpEmailLayout;
    private FirebaseAuth firebaseAuth;
    private ImageView imageView2;
    private TextView signInStartTextView,signInStartTextView2;
    private Button signInBtn;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findingViews();
        firebaseAuth = FirebaseAuth.getInstance();
        setTouchListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListeners() {
        signUpNameText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                signUpNameLayout.setErrorEnabled(false);
                return false;
            }
        });
        signUpSurnameText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                signUpSurnameLayout.setErrorEnabled(false);
                return false;
            }
        });
        signUpEmailText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                signUpEmailLayout.setErrorEnabled(false);
                return false;
            }
        });
    }
    public  void findingViews(){
        signUpNameText = findViewById(R.id.sign_up_name_edit_text);
        signUpSurnameText = findViewById(R.id.sign_up_surname_edit_text);
        signUpEmailText = findViewById(R.id.sign_up_email_edit_text);
        signUpNameLayout = findViewById(R.id.sign_up_name_layout);
        signUpSurnameLayout = findViewById(R.id.sign_up_surname_layout);
        signUpEmailLayout = findViewById(R.id.sign_up_email_layout);
        progressBar=findViewById(R.id.sign_up1_progress_bar);
        imageView2=findViewById(R.id.imageView2);
        signInStartTextView=findViewById(R.id.signInStartTextView);
        signInStartTextView2=findViewById(R.id.signInStartTextView2);
        signInBtn=findViewById(R.id.signInBtn);
        linearLayout=findViewById(R.id.linearLayout);
    }
    public void toNextPage(View view) {
        if (!isConnected(SignUpActivity.this)){
            showDialog(SignUpActivity.this);
        }else {
        String name = signUpNameText.getText().toString();
        String surname = signUpSurnameText.getText().toString();
        String email = signUpEmailText.getText().toString();
        progressBar.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(name)) {
            signUpNameLayout.setError("Name can't be empty");
            progressBar.setVisibility(View.INVISIBLE);
        }
        if (TextUtils.isEmpty(surname)) {
            signUpSurnameLayout.setError("Surname can't be empty");
            progressBar.setVisibility(View.INVISIBLE);
        }
        if (TextUtils.isEmpty(email)) {
            signUpEmailLayout.setError("E-mail can't be empty");
            progressBar.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(surname) & !TextUtils.isEmpty(email)) {

            firebaseAuth.createUserWithEmailAndPassword(email,"123123").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent=new Intent(SignUpActivity.this, SignUpPage2Activity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("surname",surname);
                    intent.putExtra("email",email);
                    Pair[]pairs=new Pair[6];
                    pairs[0]=new Pair<View,String>(imageView2,"sign_in_start_logo_image");
                    pairs[1]=new Pair<View,String>(signInStartTextView,"sign_in_start_text_view");
                    pairs[2]=new Pair<View,String>(signInStartTextView2,"sign_up_to_continue");
                    pairs[3]=new Pair<View,String>(signInBtn,"sign_up_btn");
                    pairs[4]=new Pair<View,String>(linearLayout,"sign_up_already_have_layout");
                    pairs[5]=new Pair<View,String>(signUpNameLayout,"to_date_picker");
                    ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this,pairs);
                    startActivity(intent,activityOptions.toBundle());
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    signUpEmailLayout.setError(e.getLocalizedMessage());
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
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
        onBackPressed();
    }

}

