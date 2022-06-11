package com.example.balante_midterm;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    ImageView game_iv;
    Uri gameImage;
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

    public void addData(String title, Timestamp date, String dev, String pub, String gen) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Adding game, please wait...");
        dialog.setCancelable(false);
        dialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyhhmm", Locale.US);
        String id = mUser.getUid() + dateFormat.format(new Date());
        FirebaseStorage storage = FirebaseStorage.getInstance();

// Create the file metadata
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();
        StorageReference storageRef = storage.getReference().child("image/"+mUser.getUid()+"/"+dateFormat.format(new Date()));
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
                    Game game = new Game(id, title, date, dev, pub, gen,downloadUri.toString());
                    db.collection("Games").document(id).set(game).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Add.this, "Game successfully added!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(),View_All.class));
                            }
                            else {
                                dialog.dismiss();
                                AlertDialog.Builder alert = new AlertDialog.Builder(Add.this);
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
                    AlertDialog.Builder alert = new AlertDialog.Builder(Add.this);
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

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        gDate.setText(dateFormat.format(myCalendar.getTime()));
    }
}
