package com.example.balante_midterm;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText title, date, developer, publisher, genre;
    Button create, viewall;
    DBHelper DB;
    Button btnLogOut;
    FirebaseAuth mAuth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.game_titleEdit);
        date = findViewById(R.id.game_titleEdit);
        developer = findViewById(R.id.game_developerEdit);
        publisher = findViewById(R.id.game_publisherEdit);
        genre = findViewById(R.id.game_genreEdit);

        create = findViewById(R.id.create_button);
        viewall = findViewById(R.id.viewall_button);
        btnLogOut = findViewById(R.id.logout_button);
        mAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        create.setOnClickListener(view ->{
            startActivity(new Intent(MainActivity.this, Add.class));
        });

        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, View_All.class));
            }
        });


    }
}