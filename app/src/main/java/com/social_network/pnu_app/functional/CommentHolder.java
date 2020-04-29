package com.social_network.pnu_app.functional;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.social_network.pnu_app.pages.MainStudentPage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Date;


import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;


public class CommentHolder extends AppCompatActivity {

    DatabaseReference referenceMyPostComent;
    long idTime;
    long countPost;
    String currentPostedUser;
    String namePost;
    String lastNamePost;
    long time;
    String timePost;



    public void addCommentToDatabase(String keyPost, String comment, String senderUserId) {
        idTime = new Date().getTime();
        referenceMyPostComent = FirebaseDatabase.getInstance().getReference("students").child(senderUserId)
                .child("Posts").child(keyPost).child("Comments").push();

        referenceMyPostComent.setValue(new Comment(senderUserId, "text", comment, idTime), idTime).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public void deleteCommentToDatabase(String keyPost,String keyComment, String senderUserId) {

        referenceMyPostComent = FirebaseDatabase.getInstance().getReference("students").child(senderUserId)
                .child("Posts").child(keyPost).child("Comments").child(keyComment);

        referenceMyPostComent.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


    public void holderComment(final String senderUserId, final RecyclerView recyclerViewComment, final String keyPost) {
        Query myCommetnsReference = FirebaseDatabase.getInstance().getReference("students").child(senderUserId)
                .child("Posts").child(keyPost).child("Comments");
        final DatabaseReference allStudentsReference = FirebaseDatabase.getInstance().getReference("students");


        FirebaseRecyclerAdapter<Comment, CommentViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>
                (
                        Comment.class,
                        R.layout.comment_layout,
                        CommentViewHolder.class,
                        myCommetnsReference

                ) {
            @Override
            protected void populateViewHolder(final CommentViewHolder commentViewHolder, final Comment comment, int i) {

                recyclerViewComment.scrollToPosition(i);
                /////
                final String currentKeyComment = getRef(i).getKey();
                commentViewHolder.actionButton(senderUserId, currentKeyComment, comment.getKeySender(),keyPost);
                allStudentsReference.child(comment.getKeySender()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        namePost = dataSnapshot.child("name").getValue().toString();
                        lastNamePost = dataSnapshot.child("lastName").getValue().toString();

                        CommentNestedHolder commentNestedHolder = new CommentNestedHolder();
                        commentNestedHolder.holderNestedComment(senderUserId, commentViewHolder.getRecyclerViewComentComment(), keyPost ,currentKeyComment);

                        String linkFirebaseStorageMainPhoto;
                        try {
                            linkFirebaseStorageMainPhoto = dataSnapshot.child("linkFirebaseStorageMainPhoto").getValue().toString();
                        }
                        catch (NullPointerException nullPointerException){
                            linkFirebaseStorageMainPhoto ="";
                        }


                        commentViewHolder.setStudentName(namePost, lastNamePost);



                        final DatabaseReference referenceMyPostLikes =FirebaseDatabase.getInstance().getReference("students")
                                .child(senderUserId).child("Posts").child(keyPost).child("Comments").child(currentKeyComment).child("likes").child(comment.getKeySender());

                        referenceMyPostLikes.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    if (dataSnapshot.exists()) {
                                        commentViewHolder.setLikeOn();
                                    }
                                    else {
                                        commentViewHolder.setLikeOff();
                                    }
                                }
                                catch (NullPointerException e){
                                    commentViewHolder.setLikeOff();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        FirebaseDatabase.getInstance().getReference("students")
                                .child(senderUserId).child("Posts").child(keyPost).child("Comments").child(currentKeyComment).child("likes")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            dataSnapshot.getChildrenCount();
                                            commentViewHolder.setCountLike(String.valueOf(dataSnapshot.getChildrenCount()));
                                        }
                                        else{

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                        if (linkFirebaseStorageMainPhoto != "" && commentViewHolder.mView.getContext() != null) {
                            commentViewHolder.setStudentImage(commentViewHolder.mView.getContext(), linkFirebaseStorageMainPhoto);
                            commentViewHolder.setStudentComentImage(commentViewHolder.mView.getContext(), linkFirebaseStorageMainPhoto);
                        }

                        time = comment.getTime();
                        LastSeenTime objTimePost = new LastSeenTime();
                        timePost= objTimePost.getTimePost(time);

                        commentViewHolder.setTimePost(timePost);

                        commentViewHolder.setTextPost(comment.getText());


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        };
        recyclerViewComment.setAdapter(firebaseRecyclerAdapter);
    }

    public void alertDialogSettingsComment(final String keyPost, final String keyComment, Context contex, final String senderUserId){
        String mass[] = new String[] {"Видалити коментар"};
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(contex);

        a_builder.setTitle("Налаштування коментару")
                .setItems( mass, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            // Delete comment
                          deleteCommentToDatabase(keyPost,keyComment, senderUserId);
                        }

                    }
                });
        AlertDialog alert = a_builder.create();
        alert.show();
    }
}


class CommentViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public CommentViewHolder(View itemView){
        super(itemView);

        mView= itemView;
    }


    public void setOnlineImage(){
        ImageView imageOnline = mView.findViewById(R.id.img_online_comment);
        imageOnline.setVisibility(View.VISIBLE);
    }

    public void setStudentName(String studentName, String studentLastName){
        TextView nameAndLastName = mView.findViewById(R.id.comment_username);
        nameAndLastName.setText(studentName + " " + studentLastName);
    }

    public void setTimePost(String time){
        TextView timeLastMessage = mView.findViewById(R.id.comment_time);
        timeLastMessage.setText(time);
    }
    public void setTextPost(String textPost){
        TextView timeLastMessage = mView.findViewById(R.id.tvCommentContent);
        timeLastMessage.setText(textPost);
    }

    Button btnSettingComment;
    ImageView btnLikeMyComment;
    TextView tvCountLikePost;
    TextView tv_response_comment;
    LinearLayout LLCommentCommentMy;
    ImageView sendComentOnComment;
    View lineBelowEtWallComment;
    EmojiconEditText editTextCommentComment;
    RecyclerView recyclerViewComentComment;


    public void setLikeOn(){
        btnLikeMyComment.setBackground(mView.getContext().getResources().getDrawable(R.drawable.btn_like_press));
    }

    public void setLikeOff(){
        btnLikeMyComment.setBackground(mView.getContext().getResources().getDrawable(R.drawable.btn_like_unpress));
    }

    public RecyclerView getRecyclerViewComentComment(){
        recyclerViewComentComment= mView.findViewById(R.id.recyclerViewComentComment);
        recyclerViewComentComment.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        return recyclerViewComentComment;
    }


    public void setCountLike(String countLike){
        tvCountLikePost = mView.findViewById(R.id.tvCountLikeComment);
        tvCountLikePost.setText(countLike);
    }


    public void actionButton(final String senderUserId, final String keyComment, final String ketSetterComment, final String keyPost) {
        final DatabaseReference referenceMyCommentLikes =FirebaseDatabase.getInstance().getReference("students")
                .child(senderUserId).child("Posts").child(keyPost).child("Comments").child(keyComment).child("likes");

        btnSettingComment = mView.findViewById(R.id.btnSettingComment);
        btnLikeMyComment = mView.findViewById(R.id.btnLikeMyComment);
        LLCommentCommentMy = mView.findViewById(R.id.LLCommentCommentMy);
        tv_response_comment = mView.findViewById(R.id.tv_response_comment);
        sendComentOnComment = mView.findViewById(R.id.sendComentOnComment);
        lineBelowEtWallComment = mView.findViewById(R.id.lineBelowEtWallComment);

        tv_response_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LLCommentCommentMy.setVisibility(View.VISIBLE);
                sendComentOnComment.setVisibility(View.VISIBLE);
                lineBelowEtWallComment.setVisibility(View.VISIBLE);
            }
        });

        btnSettingComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSettingComment.setEnabled(false);
                CommentHolder commentHolder = new CommentHolder();
                commentHolder.alertDialogSettingsComment(keyPost, keyComment, mView.getContext(), senderUserId);
                btnSettingComment.setEnabled(true);
            }
        });

        btnLikeMyComment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                referenceMyCommentLikes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot.hasChild(ketSetterComment)) {
                                referenceMyCommentLikes.child(ketSetterComment).removeValue()
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
                                referenceMyCommentLikes.child(ketSetterComment).setValue(System.currentTimeMillis())
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

        editTextCommentComment = mView.findViewById(R.id.editTextCommentComment);
        sendComentOnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkStatus network = new NetworkStatus();
                if (!network.isOnline()) {
                    // progressBar.setVisibility(View.GONE);
                    Toast.makeText(mView.getContext(), " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                } else {

                    String commentComment = editTextCommentComment.getText().toString();
                    System.out.println("comment = " + commentComment);
                    if (!commentComment.equals("")) {
                        CommentNestedHolder commentNestedHolder= new CommentNestedHolder();
                        commentNestedHolder.addCommentNestedToDatabase(keyPost, commentComment, senderUserId, keyComment);
                        editTextCommentComment.setText("");
                    } else {
                        Toast.makeText(mView.getContext(), "Введіть запис!",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }

    public void setStudentComentImage(final Context context, final String studentImage) {
        final CircleImageView image = mView.findViewById(R.id.comment_profile_image_coment);

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
        final CircleImageView image = mView.findViewById(R.id.comment_profile_image);

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



