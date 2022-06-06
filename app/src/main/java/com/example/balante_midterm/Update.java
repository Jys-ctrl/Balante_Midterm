package com.example.balante_midterm;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Update extends AppCompatActivity {
    EditText gDate, gTit, gDev, gPub, gGen;
    Button updatebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        gTit = findViewById(R.id.game_titleEdit);
        gDate = (EditText) findViewById(R.id.game_dateEdit);
        gDev = findViewById(R.id.game_developerEdit);
        gPub = findViewById(R.id.game_publisherEdit);
        gGen = findViewById(R.id.game_genreEdit);
        updatebutton = findViewById(R.id.updatebutton);
        if (getIntent().getExtras() != null) {
            Game game = (Game) getIntent().getExtras().getParcelable("Game");
            String myFormat = "MM/dd/yy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
            gTit.setText(game.getgTit());
            gDate.setText(dateFormat.format(game.getgDate().toDate()));
            gDev.setText(game.getgDev());
            gPub.setText(game.getgPub());
            gGen.setText(game.getgGen());

            updatebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    game.setgTit(gTit.getText().toString());
                    game.setgDev(gDev.getText().toString());
                    game.setgPub(gPub.getText().toString());
                    game.setgGen(gGen.getText().toString());
//                  gDate.setText(dateFormat.format(myCalendar.getTime()));

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Games").document(game.getgID()).set(game).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Update.this, "Game successfully updated!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                AlertDialog.Builder alert = new AlertDialog.Builder(Update.this);
                                alert.setCancelable(false);
                                alert.setTitle("Error!");
                                alert.setMessage(task.getException().getLocalizedMessage());
                                alert.setPositiveButton("Okay", null);
                                alert.show();
                            }
                        }
                    });

                }
            });
        }

    }
}
