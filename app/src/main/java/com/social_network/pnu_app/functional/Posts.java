package com.social_network.pnu_app.functional;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.social_network.pnu_app.entity.Post;
import com.social_network.pnu_app.localdatabase.AppDatabase;
import com.social_network.pnu_app.pages.MainStudentPage;
import com.social_network.pnu_app.pages.ProfileStudent;

import java.util.Date;


public class Posts extends AppCompatActivity {


    DatabaseReference referenceMyPost;
    long idTime;

    public void addPostToDatabase(String post ,String senderUserId){
        referenceMyPost = FirebaseDatabase.getInstance().getReference("students").child(senderUserId).child("Posts");
        idTime= new Date().getTime();
        referenceMyPost.push().setValue(new Post(senderUserId, "text", post,  idTime)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });;

      //  if (editTextPost.getText() != null) {

     //   }

    }


}
