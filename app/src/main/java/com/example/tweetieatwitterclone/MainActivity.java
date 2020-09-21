    package com.example.tweetieatwitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

    public class MainActivity extends AppCompatActivity {
        EditText username;
        EditText pass;
        FirebaseAuth mauth;
        DatabaseReference database;


    // redirect user activity function


    public void redirect_user(){

        if(mauth.getCurrentUser()!= null){
            Intent activity_user = new Intent(getApplicationContext(),UserActivity.class);
            activity_user.putExtra("userid",mauth.getCurrentUser().getUid());
            startActivity(activity_user);

        }
    }



    public void login(View view){

        username = findViewById(R.id.user);
        pass = findViewById(R.id.pass);
        mauth = FirebaseAuth.getInstance();



        mauth.signInWithEmailAndPassword(username.getText().toString(), pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(),"logged in",Toast.LENGTH_LONG).show();
                            redirect_user();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("failed", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext()," couldnt logged in",Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });




    }



    public void signupLogin(View view){

        username = findViewById(R.id.user);
        pass = findViewById(R.id.pass);
        mauth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Users");


        // create user
        mauth.createUserWithEmailAndPassword(username.getText().toString(), pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(),"Signed up",Toast.LENGTH_LONG).show();
                            FirebaseUser user = mauth.getCurrentUser();



                            database.child(user.getUid()).child("email").setValue(username.getText().toString());
                            database.child(user.getUid()).child("password").setValue(pass.getText().toString());

                            Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_LONG).show();




                            // database write



                            mauth.signOut();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(),"Signing up failed",Toast.LENGTH_LONG).show();

                        }

                    }
                });







    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setTitle("Tweetie: Login");











    }
}