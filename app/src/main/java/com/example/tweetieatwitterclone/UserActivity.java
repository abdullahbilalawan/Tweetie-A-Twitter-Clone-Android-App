package com.example.tweetieatwitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    ArrayList<String> users;
    ArrayAdapter adapter;
    ListView listView;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    Intent intent;
    String id;
    FirebaseFirestore db;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu,menu);




        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.tweet){


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Send a Tweet");
            final EditText text = new EditText(this);

            builder.setView(text);
            builder.setPositiveButton("send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if(text.getText().toString() != null){
                        Map<String, Object> user = new HashMap<>();
                        user.put("Tweet", text.getText().toString());

                        // firestore reference
                        db = FirebaseFirestore.getInstance();
                        // Add a new document with a generated ID
                        db.collection("users")
                                .add(user)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.i("DOC", "DocumentSnapshot added with ID: " + documentReference.getId());
                                        Toast.makeText(getApplicationContext(),"Tweet sent",Toast.LENGTH_LONG).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("DOC", "Error adding document", e);
                                        Toast.makeText(getApplicationContext(),"error try again later",Toast.LENGTH_LONG).show();
                                    }
                                });











                    }
                    else{
                        Toast.makeText(getApplicationContext(),"error try again later",Toast.LENGTH_LONG).show();
                    }





                }
            });

            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.cancel();
                }
            });

            builder.show();
        }
        else if(item.getItemId()==R.id.logout){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);


        }
        else if(item.getItemId()==R.id.userfeed){

            Intent userfeed = new Intent(getApplicationContext(),FeedActivity.class);
            startActivity(userfeed);

        }







        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle("Tweetie: Users");

        // intent
        intent = getIntent();
         id = intent.getStringExtra("userid");

        // objects

        listView= findViewById(R.id.textfile);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        users= new ArrayList();
        users.add("Default user");
        adapter= new ArrayAdapter(this,android.R.layout.simple_list_item_checked,users);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                if(checkedTextView.isChecked()){

                    // if checked followed
                    // follow logic code
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users"+"/"+id);
                    databaseReference.child("current user follow"+i).setValue(users.get(i));






                }else{

                    //if unchecked not followed

                }
            }
        });


        // listview
        databaseReference = FirebaseDatabase.getInstance().getReference("Users"+"/"+id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    String user_name=snapshot.child("email").getValue().toString();
                    users.add(user_name);
                    adapter.notifyDataSetChanged();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });












    }
}