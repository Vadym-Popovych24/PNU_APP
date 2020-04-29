package com.social_network.pnu_app.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.social_network.pnu_app.R;

import java.util.ArrayList;

public class ActivityListLikesPost extends AppCompatActivity {


    DatabaseReference referenceLikesPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_likes_post);

        Intent intentGetKeysPost= getIntent();
        ArrayList<String> listKeys = intentGetKeysPost.getStringArrayListExtra("keysPost");

        System.out.println("keySender = " + listKeys.get(0));
        System.out.println("keyPost= " + listKeys.get(1));

      //  referenceLikesPost = FirebaseDatabase.getInstance().getReference("students")
           //     .child(senderUserId).child("Posts").child(keyPost).child("likes");

    }
}
