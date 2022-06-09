package com.example.balante_midterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

import android.app.Activity;

import androidx.annotation.NonNull;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    TextInputEditText emailText, passwordText, confirmPasswordText;
    Button registerButton;
    TextView loginNowButton;

    DatabaseReference reference;
    User value;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        loginNowButton = findViewById(R.id.loginNowButton);
        emailText = findViewById(R.id.emailAddress);
        passwordText = findViewById(R.id.password);
        confirmPasswordText = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.registerButton);

        loginNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString().trim();
                String password= passwordText.getText().toString().trim();
                String confirmPassword = confirmPasswordText.getText().toString().trim();
                if (email.isEmpty()) {
                    emailText.setError("Email is required!");
                    emailText.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailText.setError("Please provide valid Email!");
                    emailText.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    passwordText.setError("Password is required!");
                    passwordText.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    passwordText.setError("Min password length should be 6 characters!");
                    passwordText.requestFocus();
                    return;
                }
                if (confirmPassword.isEmpty()) {
                    confirmPasswordText.setError("Confirm your password!");
                    confirmPasswordText.requestFocus();
                    return;
                }
                if (!confirmPassword.matches(password)) {
                    confirmPasswordText.setError("Password doesn't match!");
                    confirmPasswordText.requestFocus();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this,"You are successfully Registered", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                Log.d("TAG", "onComplete: exist_email");
                                emailText.setError("Email already exists");
                                emailText.requestFocus();
                            } catch (Exception e) {
                                Toast.makeText(RegisterActivity.this, "Failed to register! Try Again!", Toast.LENGTH_LONG).show();
                                Log.d("TAG", "onComplete: " + e.getMessage());
                            }
                        }
                    }
                });
            }
        });
    }
}