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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.okada.travelogue.R;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ImageView imageView;
    private TextView textView,textView1;
    private Button button;
    private LinearLayout linearLayout;
    private TextInputLayout paswordEditLayout,usernameEditLayout;
    private TextInputEditText passwordEditText,usernameEditText;
    private ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        firebaseAuth=FirebaseAuth.getInstance();
        createRequest();
        findingViews();
        setTouchListeners();

    }


    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    public void signInWithGoogle(View view) {
        if (!isConnected(SignInActivity.this)){
            showDialog(SignInActivity.this);
        }else {
            progressBar.setVisibility(View.VISIBLE);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(SignInActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(SignInActivity.this,HomeActivity.class));
                            Animatoo.animateSwipeRight(SignInActivity.this);
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setTouchListeners(){
        usernameEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                usernameEditLayout.setErrorEnabled(false);

                return false;
            }
        });
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                paswordEditLayout.setErrorEnabled(false);
                return false;
            }
        });
    }
    public void findingViews(){
        imageView=findViewById(R.id.imageView2);
        textView=findViewById(R.id.signInStartTextView);
        textView1=findViewById(R.id.sign_in_to_continue);
        button=findViewById(R.id.signInBtn);
        linearLayout=findViewById(R.id.sign_in_lineer_layout);
        passwordEditText=findViewById(R.id.sign_in_password);
        usernameEditText=findViewById(R.id.sign_in_username_email);
        paswordEditLayout=findViewById(R.id.sign_in_password_layout);
        progressBar=findViewById(R.id.sign_in_progress_bar);
        usernameEditLayout=findViewById(R.id.sign_in_username_email_layout);
    }
    public void signIn(View view){
        if (!isConnected(SignInActivity.this)){
            showDialog(SignInActivity.this);
        }else {
            progressBar.setVisibility(View.VISIBLE);
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (password.matches("")) {
                progressBar.setVisibility(View.INVISIBLE);
                paswordEditLayout.setError("Password can't be empty");
            }
            if (!TextUtils.isEmpty(username)) {
                if (!password.matches("")) {
                    firebaseAuth.signInWithEmailAndPassword(username, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Intent toHomeActivity = new Intent(SignInActivity.this, HomeActivity.class);
                                    Toast.makeText(SignInActivity.this,"Logged in",Toast.LENGTH_SHORT).show();
                                    startActivity(toHomeActivity);
                                    Animatoo.animateSwipeRight(SignInActivity.this);
                                    finish();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    if (e instanceof FirebaseAuthException) {
                                        if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_WRONG_PASSWORD")) {
                                            paswordEditLayout.setError("Wrong password");
                                        } else if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_USER_NOT_FOUND")) {
                                            usernameEditLayout.setError("Wrong email");
                                        } else if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_INVALID_EMAIL")) {
                                            usernameEditLayout.setError("Invalid email");
                                        } else {
                                            Toast.makeText(SignInActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            });
                }

            } else {
                progressBar.setVisibility(View.INVISIBLE);
                usernameEditLayout.setError("E-mail can't be empty");

            }
        }
    }

    public void toSignUp(View view) {
        Intent toSignUpAct = new Intent(SignInActivity.this,SignUpActivity.class);
        Pair[]pairs=new Pair[7];
        pairs[0]=new Pair<View,String >(imageView,"sign_in_start_logo_image");
        pairs[1]=new Pair<View,String >(textView,"sign_in_start_text_view");
        pairs[2]=new Pair<View,String >(textView1,"sign_up_to_continue");
        pairs[3]=new Pair<View,String >(usernameEditLayout,"sign_up_email_layout");
        pairs[4]=new Pair<View,String >(paswordEditLayout,"sign_up_password_layout");
        pairs[5]=new Pair<View,String >(button,"sign_up_btn");
        pairs[6]=new Pair<View,String >(linearLayout,"sign_up_already_have_layout");
        ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(SignInActivity.this,pairs);
        startActivity(toSignUpAct,activityOptions.toBundle());
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
    public void toForgotPassword(View view) {
        Intent toForgotPasswordAct = new Intent(SignInActivity.this,ForgotPasswordActivity.class);
        Pair[]pairs1=new Pair[5];
        pairs1[0]=new Pair<View,String >(imageView,"sign_in_start_logo_image");
        pairs1[1]=new Pair<View,String >(textView,"sign_in_start_text_view");
        pairs1[2]=new Pair<View,String >(textView1,"sign_up_to_continue");
        pairs1[3]=new Pair<View,String >(usernameEditLayout,"sign_up_email_layout");
        pairs1[4]=new Pair<View,String >(button,"sign_up_btn");
        ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(SignInActivity.this,pairs1);
        startActivity(toForgotPasswordAct,activityOptions.toBundle());
    }

}