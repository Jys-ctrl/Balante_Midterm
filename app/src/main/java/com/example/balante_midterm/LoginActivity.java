package com.example.balante_midterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements OnClickListener {

    TextInputEditText emailText;
    TextInputEditText passwordText;
    TextView tvRegisterHere;
    Button loginButton;
    FirebaseAuth fauth;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    ProgressBar progressbar;
    Dialog dialog;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.emailAddress);
        passwordText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        fauth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        dialog = new Dialog(this);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerNowButton:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.loginButton:
                userLogin();
                break;

            case R.id.forgotPasswordButton:
                forgotPassword();
                break;
        }
    }

    private void userLogin() {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (email.isEmpty()) {
            emailText.setError("Email is required!");
            emailText.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Please provide valid Email!");
            emailText.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (password.isEmpty()) {
            passwordText.setError("Password is required!");
            passwordText.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (password.length() < 6) {
            passwordText.setError("Min password length should be 6 characters!");
            passwordText.requestFocus();
            progressDialog.dismiss();
            return;
        }

        progressDialog.setMessage("Logging in...Please Wait");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            progressDialog.dismiss();

                        } else {
                            try {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthInvalidUserException invalidEmail) {
                                Log.d("TAG", "onComplete: invalid_email");
                                emailText.setError("Invalid Email not registered");
                                emailText.requestFocus();
                                progressDialog.dismiss();
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                Log.d("TAG", "onComplete: wrong_password");
                                passwordText.setError("Wrong Password");
                                passwordText.requestFocus();
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                Toast.makeText(LoginActivity.this, "Failed to login!", Toast.LENGTH_LONG).show();
                                Log.d("TAG", "onComplete: " + e.getMessage());
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
    }

    private void forgotPassword() {
        dialog.setContentView(R.layout.forgotpass_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText emailText = dialog.findViewById(R.id.emailAddress);
        Button send = dialog.findViewById(R.id.sendButton);
        dialog.show();

        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                if (email.isEmpty())
                {
                    emailText.setError("Email is required!");
                    emailText.requestFocus();
                    return;
                }
                else{
                mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(LoginActivity.this, "Reset link sent to your email.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Error! Reset link not sent.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }


}