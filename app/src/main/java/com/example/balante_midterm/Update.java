package com.example.balante_midterm;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Update extends AppCompatActivity {
    EditText gDate, gTit, gDev, gPub, gGen;
    Button updatebutton;
    ImageView game_iv;
    Uri gameImage;
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 200) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                gameImage = selectedImageUri;
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    game_iv.setImageURI(selectedImageUri);
                }
            }
        }
    }

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
        game_iv = findViewById(R.id.game_iv);
        game_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
            }
        });
        if (getIntent().getExtras() != null) {
            Game game = (Game) getIntent().getExtras().getParcelable("Game");
            String myFormat = "MM/dd/yy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
            Glide.with(this).load(game.getgImg() != null ? game.getgImg() : R.drawable.ic_baseline_add_photo_alternate_24).into(game_iv);
            gTit.setText(game.getgTit());
            gDate.setText(dateFormat.format(game.getgDate().toDate()));
            gDev.setText(game.getgDev());
            gPub.setText(game.getgPub());
            gGen.setText(game.getgGen());

            updatebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    ProgressDialog dialog = new ProgressDialog(Update.this);
                    dialog.setMessage("Updating game, please wait...");
                    dialog.setCancelable(false);
                    dialog.show();

                    StorageMetadata metadata = new StorageMetadata.Builder()
                            .setContentType("image/jpeg")
                            .build();
                    StorageReference storageRef = storage.getReference().child("image/" + mUser.getUid() + "/" + dateFormat.format(new Date()));
// Upload file and metadata to the path 'images/mountains.jpg'
                    UploadTask uploadTask = storageRef.putFile(gameImage, metadata);

// Listen for state changes, errors, and completion of the upload.
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            // Continue with the task to get the download URL
                            return storageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                game.setgTit(gTit.getText().toString());
                                game.setgDev(gDev.getText().toString());
                                game.setgPub(gPub.getText().toString());
                                game.setgGen(gGen.getText().toString());
                                game.setgImg(downloadUri.toString());

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("Games").document(game.getgID()).set(game).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Update.this, "Game successfully updated!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Update.this, View_All.class));
                                        } else {
                                            AlertDialog.Builder alert = new AlertDialog.Builder(Update.this);
                                            alert.setCancelable(false);
                                            alert.setTitle("Error!");
                                            alert.setMessage(task.getException().getLocalizedMessage());
                                            alert.setPositiveButton("Okay", null);
                                            alert.show();
                                        }
                                    }
                                });
                            } else {
                                // Handle failures
                                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                                alert.setCancelable(false);
                                alert.setTitle("Error Uploading image!");
                                alert.setMessage(task.getException().getLocalizedMessage());
                                alert.setPositiveButton("Okay", null);
                                alert.show();
                                dialog.dismiss();
                                // ...
                            }
                        }
                    });


                }
            });
        }

    }
}
