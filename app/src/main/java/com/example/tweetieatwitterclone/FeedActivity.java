package com.example.tweetieatwitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ThrowOnExtraProperties;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {


    ListView listView;
    ArrayList tweets;
    ArrayAdapter adapter;


    // firestore reference
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);


        listView = findViewById(R.id.list);
        //tweets.add("Default tweet");
        tweets = new ArrayList<String>();
        adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,tweets);

        listView.setAdapter(adapter);



        Task<QuerySnapshot> users = db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String data= (String) document.get("Tweet");

                                tweets.add(data);



                                adapter.notifyDataSetChanged();

                                Toast.makeText(getApplicationContext(),"tweets loaded",Toast.LENGTH_LONG).show();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"tweets load failed",Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }
}