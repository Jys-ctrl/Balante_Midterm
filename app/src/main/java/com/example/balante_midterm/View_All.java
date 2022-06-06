package com.example.balante_midterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class View_All extends AppCompatActivity {

    GamesListAdapter listAdapter;
    List<Game> gameList;
    ListView gameLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        gameList = new ArrayList<>();
        listAdapter = new GamesListAdapter(gameList, this);
        gameLV = findViewById(R.id.gameLV);
        gameLV.setAdapter(listAdapter);
        getGames();
    }

    private void getGames(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Games")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                gameList.add(document.toObject(Game.class));
                            }
                        } else {

                        }
                        listAdapter.notifyDataSetChanged();
                    }
                });
    }
}