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
    Button create, search, update, delete;
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
        search = findViewById(R.id.search_button);
        update = findViewById(R.id.update_button);
        delete = findViewById(R.id.delete_button);

        btnLogOut = findViewById(R.id.logout_button);
        mAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String GameTitle = title.getText().toString();
                    String GameDate = date.getText().toString();
                    String GameDeveloper = developer.getText().toString();
                    String GamePublisher = publisher.getText().toString();
                    String GameGenre = genre.getText().toString();

                    if (GameTitle.isEmpty()) {
                        title.setError("Game name is required!");
                        title.requestFocus();
                        return;
                    }

                    if (GameID.isEmpty() || GameTitle.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please fill up required fields!", Toast.LENGTH_SHORT).show();
                    } else {
                        Boolean checkinsertdata = DB.insertuserdata(GameID, GameTitle, GameDate, GameDeveloper, GamePublisher, GameGenre);
                        if (checkinsertdata == true) {
                            Toast.makeText(MainActivity.this, "Game Inserted", Toast.LENGTH_SHORT).show();
                            id.getText().clear();
                            title.getText().clear();
                            date.getText().clear();
                            developer.getText().clear();
                            publisher.getText().clear();
                            genre.getText().clear();
                        } else
                            Toast.makeText(MainActivity.this, "Game Not Inserted.Game ID Exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String GameTitle = title.getText().toString();
                    String GameDate = date.getText().toString();
                    String GameDeveloper = developer.getText().toString();
                    String GamePublisher = publisher.getText().toString();
                    String GameGenre = genre.getText().toString();

                    if (GameTitle.isEmpty()) {
                        title.setError("Game title is required!");
                        title.requestFocus();
                        return;
                    }

                    if (GameID.isEmpty() || GameTitle.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please fill up required fields!", Toast.LENGTH_SHORT).show();
                    } else {
                        Boolean checkupdatedata = DB.updateuserdata( GameTitle, GameDate, GameDeveloper, GamePublisher, GameGenre);
                        if (checkupdatedata == true) {
                            Toast.makeText(MainActivity.this, "Game Updated", Toast.LENGTH_SHORT).show();
                            id.getText().clear();
                            title.getText().clear();
                            date.getText().clear();
                            developer.getText().clear();
                            publisher.getText().clear();
                            genre.getText().clear();
                        } else
                            Toast.makeText(MainActivity.this, "Game Not Updated.Game ID does not Exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String GameID = id.getText().toString();
                    Boolean checkudeletedata = DB.deletedata(GameID);

                    if (GameID.isEmpty()) {
                        id.setError("Game ID is required!");
                        id.requestFocus();
                        return;
                    }

                    if (checkudeletedata == true) {
                        Toast.makeText(MainActivity.this, "Game Deleted", Toast.LENGTH_SHORT).show();
                        id.getText().clear();
                    } else
                        Toast.makeText(MainActivity.this, "Game Not Deleted.Game ID does not Exists!", Toast.LENGTH_SHORT).show();
                }
            });

            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cursor res = DB.getdata();
                    if (res.getCount() == 0) {
                        Toast.makeText(MainActivity.this, "No Game/s Exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    StringBuffer buffer = new StringBuffer();
                    while (res.moveToNext()) {
                        buffer.append("ID: " + res.getString(0) + "\n");
                        buffer.append("Title: " + res.getString(1) + "\n");
                        buffer.append("Date: " + res.getString(2) + "\n");
                        buffer.append("Publisher: " + res.getString(3) + "\n");
                        buffer.append("Developer: " + res.getString(4) + "\n\n");
                        buffer.append("Genre: " + res.getString(5) + "\n\n\n");

                    }

                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Product Entries");
                    builder.setMessage(buffer.toString());
                    builder.show();
                }
            });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}