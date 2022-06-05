package com.example.balante_midterm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Add extends AppCompatActivity {

    private DatabaseReference mDatabase;
    Button save_button;
    EditText edtname, edtage, edtgender, edtcontact, edtaddress;
    EditText gTit, gDev, gPub, gGen;
    View gDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_button);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        mDatabase = db.getReference();

        gTit = findViewById(R.id.game_titleEdit);
        gDate = findViewById(R.id.game_dateEdit);
        gDev = findViewById(R.id.game_developerEdit);
        gPub = findViewById(R.id.game_publisherEdit);
        gGen = findViewById(R.id.game_genreEdit);

        save_button = findViewById(R.id.addbutton);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String saveTitle = gTit.getText().toString();
                Timestamp saveDate = gDate.getText().toString();
                String saveDev = gDev.getText().toString();
                String savePub = gPub.getText().toString();
                String saveGen = gGen.getText().toString();

                addData(saveTitle, saveDate, saveDev, savePub, saveGen);
            }
        });
    }
    public void addData(String title, Timestamp date, String dev, String pub, String gen){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Game game = new Game(title, date, dev, pub, gen);
        db.collection("cities").document("LA").set();
    }
}