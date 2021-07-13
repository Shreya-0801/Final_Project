package com.example.note_trial;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.note_trial.databinding.ActivityCreateNoteBinding;
import com.example.note_trial.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {
    //Initializing the ID's
    EditText createTitle,createContent;
    FloatingActionButton saveNote;
    // To save particular id for particular person we are using firebase authentication.
    FirebaseAuth auth;
    // To identify the user we are using firebase user
    FirebaseUser user;
    //To access the database we need FirebaseDatabase
    FirebaseDatabase database;
    Model model;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        Toolbar toolbar=findViewById(R.id.toolbarofcreatenote);
        setSupportActionBar(toolbar);
        //To display return button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Assigning the ID's
        createTitle=findViewById(R.id.createtitleofnote);
        createContent=findViewById(R.id.createcontentofnote);
        //storing the Instance
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        //It will give the instance of current user.
        user =FirebaseAuth.getInstance().getCurrentUser();
        //Here we are creating a string userid. So, that every user will get a particular id.
        String userId= user.getUid();
        saveNote=findViewById(R.id.savenote);
        //we  are creating a set Onclick listener so that, when we click save note it will show an action.
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Here we are creating a string and fetching the data which is entered by the user.
                String title = createTitle.getText().toString();
                String content = createContent.getText().toString();
                //Here we are giving the condition if any of the field(title or content) is empty then it will give a toast message Fields cannot be empty.
                if(title.isEmpty() || content.isEmpty())
                {
                    Toast.makeText(CreateNote.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    //Reference
                    // Here we are creating the object of document reference because we are storing the data in Firebase database using document reference.
                    // By writing userid we will get id of the current user and we can easily store the data of that particular user
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notes/"+userId);
                    Map<String ,Object> note= new HashMap<>();
                    //  Here we are passing the data to note
                    note.put("title",title);
                    note.put("content",content);
                    // here we are pushing the data to reference.
                    reference.push().setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(CreateNote.this, "Note created successfully!", Toast.LENGTH_SHORT).show();
                            //If note created successfully then we will move from CreateNote to NotesActivity.
                            startActivity(new Intent(CreateNote.this,NotesActivity.class));

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //If note not created successfully then it will show a msg that Failed to create Note.
                                    Toast.makeText(CreateNote.this, "Failed To Create Note", Toast.LENGTH_SHORT).show();



                                }
                            });
                }
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}