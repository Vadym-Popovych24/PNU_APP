package com.social_network.pnu_app.functional;



import android.app.usage.NetworkStats;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.R;

import com.social_network.pnu_app.entity.Comment;
import com.social_network.pnu_app.entity.Post;

import com.social_network.pnu_app.network.NetworkStatus;
import com.social_network.pnu_app.pages.ActivityListLikesPost;
import com.social_network.pnu_app.pages.FindNewFriends;
import com.social_network.pnu_app.pages.MainStudentPage;
import com.social_network.pnu_app.pages.ProfileStudent;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;


public class PostHolder extends AppCompatActivity {

    DatabaseReference referenceMyPost;
    long idTime;
    long countPost;
    String currentPostedUser;
    String namePost;
    String lastNamePost;
    long time;
    String timePost;



    public void addPostToDatabase(String post, String senderUserId) {
        idTime = new Date().getTime();
        long minusIdTime = -1 * idTime;
        referenceMyPost = FirebaseDatabase.getInstance().getReference("students").child(senderUserId).child("Posts").push();

        referenceMyPost.setValue(new Post(senderUserId, "text", post, idTime), minusIdTime).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public void deletePostToDatabase(String keyPost, String senderUserId) {

        referenceMyPost = FirebaseDatabase.getInstance().getReference("students").child(senderUserId).child("Posts").child(keyPost);

        referenceMyPost.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


    public void holderPost(final String senderUserId, final TextView tvTextWall, final RecyclerView recyclerViewPost) {
       Query myPostsReference = FirebaseDatabase.getInstance().getReference("students").child(senderUserId).child("Posts").orderByPriority();
       final DatabaseReference allStudentsReference = FirebaseDatabase.getInstance().getReference("students");
        final CommentHolder commentHolder = new CommentHolder();
        FirebaseDatabase.getInstance().getReference("students").child(senderUserId).child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countPost = dataSnapshot.getChildrenCount();
                if (countPost > 0){
                    tvTextWall.setVisibility(View.GONE);
                    //   holderPost();
                }
                else {
                    tvTextWall.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Post, PostViewHolder>
                (
                        Post.class,
                        R.layout.post_layout,
                        PostViewHolder.class,
                        myPostsReference

                ) {
            @Override
            protected void populateViewHolder(final PostViewHolder postViewHolder, final Post post, final int i) {

                final String currentKeyPost = getRef(i).getKey();
                /////
                recyclerViewPost.scrollToPosition(i);
                allStudentsReference.child(senderUserId).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        namePost = dataSnapshot.child("name").getValue().toString();
                        lastNamePost = dataSnapshot.child("lastName").getValue().toString();

                       commentHolder.holderComment(senderUserId, postViewHolder.getRecyclerViewComentPost(), currentKeyPost);


                        String linkFirebaseStorageMainPhoto;
                        try {
                            linkFirebaseStorageMainPhoto = dataSnapshot.child("linkFirebaseStorageMainPhoto").getValue().toString();
                        }
                        catch (NullPointerException nullPointerException){
                            linkFirebaseStorageMainPhoto ="";
                        }


                        postViewHolder.setStudentName(namePost, lastNamePost);;
                        postViewHolder.actionButton(senderUserId, currentKeyPost, post.getKeySender());


                        final DatabaseReference referenceMyPostLikes =FirebaseDatabase.getInstance().getReference("students")
                                .child(senderUserId).child("Posts").child(currentKeyPost).child("likes");

                        referenceMyPostLikes.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    if (dataSnapshot.hasChild(post.getKeySender())) {
                                        postViewHolder.setLikeOn();
                                    }
                                    else {
                                        postViewHolder.setLikeOff();
                                    }
                                }
                                catch (NullPointerException e){
                                    postViewHolder.setLikeOff();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        // Count Likes
                        FirebaseDatabase.getInstance().getReference("students")
                                .child(senderUserId).child("Posts").child(currentKeyPost).child("likes")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            dataSnapshot.getChildrenCount();
                                            postViewHolder.setCountLike(String.valueOf(dataSnapshot.getChildrenCount()));
                                        }
                                        else{

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                        // Count Comment

                        FirebaseDatabase.getInstance().getReference("students")
                                .child(senderUserId).child("Posts").child(currentKeyPost).child("Comments")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            dataSnapshot.getChildrenCount();
                                            postViewHolder.setCountComment(String.valueOf(dataSnapshot.getChildrenCount()));
                                        }
                                        else{

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                        if (linkFirebaseStorageMainPhoto != "" && postViewHolder.mView.getContext() != null) {
                            postViewHolder.setStudentImage(postViewHolder.mView.getContext(), linkFirebaseStorageMainPhoto);
                            postViewHolder.setStudentComentImage(postViewHolder.mView.getContext(), linkFirebaseStorageMainPhoto);
                        }

                        time = post.getTime();
                        LastSeenTime objTimePost = new LastSeenTime();
                        timePost= objTimePost.getTimePost(time);

                        postViewHolder.setTimePost(timePost);

                        postViewHolder.setTextPost(post.getText());


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        };
        recyclerViewPost.setAdapter(firebaseRecyclerAdapter);
    }

    public void alertDialogSettingsPost(final String keyPost, Context contex, final String senderUserId){
        String mass[] = new String[] {"Видалити пост"};
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(contex);

        a_builder.setTitle("Налаштування запису")
                .setItems( mass, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            // Delete post
                            deletePostToDatabase(keyPost, senderUserId);
                        }

                    }
                });
        AlertDialog alert = a_builder.create();
        alert.show();
    }
}


class PostViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public PostViewHolder(View itemView){
        super(itemView);

        mView= itemView;
    }


    public void setOnlineImage(){
        ImageView imageOnline = mView.findViewById(R.id.img_online_post);
        imageOnline.setVisibility(View.VISIBLE);
    }

    public void setStudentName(String studentName, String studentLastName){
        TextView nameAndLastName = mView.findViewById(R.id.post_username);
        nameAndLastName.setText(studentName + " " + studentLastName);
    }

    public void setTimePost(String time){
        TextView timeLastMessage = mView.findViewById(R.id.post_time);
        timeLastMessage.setText(time);
    }
    public void setTextPost(String textPost){
        TextView timeLastMessage = mView.findViewById(R.id.tvPostContent);
        timeLastMessage.setText(textPost);
    }

    Button btnSettingsPost;
    ImageView btnLikeMyPost;
    ImageView sendComentOnPost;
    RecyclerView recyclerViewComentPost;
    EmojiconEditText editTextCommentPost;
    TextView tvCountLikePost;
    public TextView tvCountComentPost;

    public RecyclerView getRecyclerViewComentPost(){
        recyclerViewComentPost= mView.findViewById(R.id.recyclerViewComentPost);
        recyclerViewComentPost.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        return recyclerViewComentPost;
    }

    public void setLikeOn(){
        btnLikeMyPost.setBackground(mView.getContext().getResources().getDrawable(R.drawable.btn_like_press));
    }

    public void setLikeOff(){
        btnLikeMyPost.setBackground(mView.getContext().getResources().getDrawable(R.drawable.btn_like_unpress));
    }



    public void setCountLike(String countLike){
        tvCountLikePost = mView.findViewById(R.id.tvCountLikePost);
        tvCountLikePost.setText(countLike);
    }

    public void setCountComment(String countComment){
        tvCountComentPost = mView.findViewById(R.id.tvCountComentPost);
        tvCountComentPost.setText(countComment);
    }


    public void actionButton(final String senderUserId, final String keyPost, final String keySetterLike) {
        final DatabaseReference referenceMyPostLikes =FirebaseDatabase.getInstance().getReference("students")
                .child(senderUserId).child("Posts").child(keyPost).child("likes");

        btnSettingsPost = mView.findViewById(R.id.btnSettingPost);
        btnLikeMyPost =mView.findViewById(R.id.btnLikeMyPost);
        sendComentOnPost = mView.findViewById(R.id.sendComentOnPost);
        tvCountComentPost = mView.findViewById(R.id.tvCountComentPost);
        tvCountLikePost =  mView.findViewById(R.id.tvCountLikePost);

        btnSettingsPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSettingsPost.setEnabled(false);
                PostHolder posts = new PostHolder();
                posts.alertDialogSettingsPost(keyPost, mView.getContext(), senderUserId);
                btnSettingsPost.setEnabled(true);
            }
        });

        btnLikeMyPost.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                referenceMyPostLikes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot.hasChild(keySetterLike)) {
                                referenceMyPostLikes.child(keySetterLike).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                //         setLikeOn();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            } else {
                                referenceMyPostLikes.child(keySetterLike).setValue(System.currentTimeMillis())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                //        setLikeOff();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        }
                        catch (NullPointerException e){}

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        editTextCommentPost = mView.findViewById(R.id.editTextCommentPost);
        sendComentOnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkStatus network = new NetworkStatus();
                if (!network.isOnline()) {
                    // progressBar.setVisibility(View.GONE);
                    Toast.makeText(mView.getContext(), " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                } else {

                    String comment = editTextCommentPost.getText().toString();
                    System.out.println("comment = " + comment);
                    if (!comment.equals("")) {
                        CommentHolder commentHolder= new CommentHolder();
                        commentHolder.addCommentToDatabase(keyPost, comment, senderUserId);
                        editTextCommentPost.setText("");
                    } else {
                        Toast.makeText(mView.getContext(), "Введіть запис!",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        tvCountLikePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keySender = keySetterLike;

                final List<String> postKeys = new ArrayList<String>();

                postKeys.add(0, keySender);
                postKeys.add(1,keyPost);
                Intent intentListLikesPost = new Intent(mView.getContext(), ActivityListLikesPost.class);
                intentListLikesPost.putStringArrayListExtra("keysPost", (ArrayList<String>) postKeys);
                mView.getContext().startActivity(intentListLikesPost);
            }
        });

    }

    public void setStudentComentImage(final Context context, final String studentImage) {
        final CircleImageView image = mView.findViewById(R.id.post_profile_image_coment);

        Picasso.with(context)
                .load(studentImage)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_close)
                .centerCrop()
                .fit()
                //.resize(1920,2560)
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        if (studentImage != null) {
                            if (!studentImage.isEmpty()) {
                                Picasso.with(context)
                                        .load(studentImage)
                                        .placeholder(R.drawable.logo_pnu)
                                        .error(R.drawable.com_facebook_close)
                                        .centerCrop()
                                        .fit()
                                        //.resize(1920,2560)
                                        .into(image);
                            }
                        } else {
                            Picasso.with(context)
                                    .load(R.drawable.com_facebook_profile_picture_blank_square)
                                    .placeholder(R.drawable.logo_pnu)
                                    .error(R.drawable.com_facebook_close)
                                    .centerCrop()
                                    .fit()
                                    //.resize(1920,2560)
                                    .into(image);
                        }

                    }
                });

    }

    public void setStudentImage(final Context context, final String studentImage) {
        final CircleImageView image = mView.findViewById(R.id.post_profile_image);

        Picasso.with(context)
                .load(studentImage)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_close)
                .centerCrop()
                .fit()
                //.resize(1920,2560)
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        if (studentImage != null) {
                            if (!studentImage.isEmpty()) {
                                Picasso.with(context)
                                        .load(studentImage)
                                        .placeholder(R.drawable.logo_pnu)
                                        .error(R.drawable.com_facebook_close)
                                        .centerCrop()
                                        .fit()
                                        //.resize(1920,2560)
                                        .into(image);
                            }
                        } else {
                            Picasso.with(context)
                                    .load(R.drawable.com_facebook_profile_picture_blank_square)
                                    .placeholder(R.drawable.logo_pnu)
                                    .error(R.drawable.com_facebook_close)
                                    .centerCrop()
                                    .fit()
                                    //.resize(1920,2560)
                                    .into(image);
                        }

                    }
                });

    }
}



