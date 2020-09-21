package com.example.tweetieatwitterclone;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;
@IgnoreExtraProperties
public class User implements Serializable {

    String email;
    String password;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
