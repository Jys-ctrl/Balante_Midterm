package com.example.balante_midterm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class GamesListAdapter extends BaseAdapter {

    List<Game> gameList;
    Context context;

    public GamesListAdapter(List<Game> gameList, Context context) {
        this.gameList = gameList;
        this.context = context;


    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        view = LayoutInflater.from(context).inflate(R.layout.gameslistitem, null);
        TextView gameTitle_TV, gameDate_TV, gameDeveloper_TV, gamePublisher_TV, gameGenre_TV;
        ImageView editIB, deleteIB,image_iv;
        editIB = view.findViewById(R.id.editIB);
        deleteIB = view.findViewById(R.id.deleteIB);
        gameTitle_TV = view.findViewById(R.id.gameTitle_TV);
        gameDate_TV = view.findViewById(R.id.gameDate_TV);
        gameDeveloper_TV = view.findViewById(R.id.gameDeveloper_TV);
        gamePublisher_TV = view.findViewById(R.id.gamePublisher_TV);
        gameGenre_TV = view.findViewById(R.id.gameGenre_TV);
        image_iv=view.findViewById(R.id.image_iv);
        Glide.with(context).load(gameList.get(i).getgImg()).into(image_iv);
        gameTitle_TV.setText("Title: " + gameList.get(i).getgTit());
        gameDate_TV.setText("Date Created: " + dateFormat.format(gameList.get(i).getgDate().toDate()));
        gameDeveloper_TV.setText("Developer: " + gameList.get(i).getgDev());
        gamePublisher_TV.setText("Publisher: " + gameList.get(i).getgPub());
        gameGenre_TV.setText("Genre: " + gameList.get(i).getgGen());

        editIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Update.class);
                intent.putExtra("Game", (Parcelable) gameList.get(i));
                context.startActivity(intent);
            }
        });

        deleteIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setCancelable(false);
                alert.setTitle("Delete game record");
                alert.setMessage("Are you sure to delete this game record?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int o) {

                        db.collection("Games").document(gameList.get(i).getgID())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        gameList.remove(i);
                                        notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
            }
        });

        return view;
    }
}
