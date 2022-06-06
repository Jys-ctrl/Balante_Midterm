package com.example.balante_midterm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Add extends AppCompatActivity {

    private DatabaseReference mDatabase;
    Button save_button;
    EditText gDate, gTit, gDev, gPub, gGen;
    final Calendar myCalendar = Calendar.getInstance();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        gTit = findViewById(R.id.game_titleEdit);
        gDate = (EditText) findViewById(R.id.game_dateEdit);
        gDev = findViewById(R.id.game_developerEdit);
        gPub = findViewById(R.id.game_publisherEdit);
        gGen = findViewById(R.id.game_genreEdit);
        save_button = findViewById(R.id.addbutton);


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        gDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Add.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saveTitle = gTit.getText().toString();
                Timestamp saveDate = new Timestamp(myCalendar.getTime());
                String saveDev = gDev.getText().toString();
                String savePub = gPub.getText().toString();
                String saveGen = gGen.getText().toString();

                addData(saveTitle, saveDate, saveDev, savePub, saveGen);
            }
        });
    }

    public void addData(String title, Timestamp date, String dev, String pub, String gen) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyhhmm", Locale.US);
        String id = mUser.getUid() + dateFormat.format(new Date());
        Game game = new Game(id, title, date, dev, pub, gen);
        db.collection("Games").document(id).set(game).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Add.this, "Game successfully added!", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(Add.this);
                    alert.setCancelable(false);
                    alert.setTitle("Error!");
                    alert.setMessage(task.getException().getLocalizedMessage());
                    alert.setPositiveButton("Okay", null);
                    alert.show();
                }
            }
        });


    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        gDate.setText(dateFormat.format(myCalendar.getTime()));
    }
}
