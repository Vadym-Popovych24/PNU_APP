package com.social_network.pnu_app.pages;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.MySubscribers;
import com.social_network.pnu_app.functional.AcceptFriendRecyclerView;
import com.social_network.pnu_app.localdatabase.AppDatabase;
import com.social_network.pnu_app.network.NetworkStatus;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MySubscribedsFragment extends Fragment {
    private View myMainView;
    ProgressBar progressBar;

    private RecyclerView mySubscribedsList;
    private DatabaseReference mySubscribedsReference;
    private DatabaseReference students;

    private TextView textViewDefaultText;
    private TextView textViewMySubscribeds;
    String SerieIDCard;

    String senderUserId;
    long countSubscribeds;
    NetworkStatus network = new NetworkStatus();
    static Context myContex;

    public MySubscribedsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myMainView = inflater.inflate(R.layout.fragment_my_subscribeds, container, false);

        textViewDefaultText = myMainView.findViewById(R.id.defaultTextListMySubscribeds);

        textViewMySubscribeds = myMainView.findViewById(R.id.textViewMySubscribeds);

        progressBar = myMainView.findViewById(R.id.progressBarMySubscribeds);
        progressBar.setVisibility(View.VISIBLE);

        mySubscribedsList = myMainView.findViewById(R.id.mySubscribedsRecyclerView);
        ProfileStudent profileStudent = new ProfileStudent();
        senderUserId = profileStudent.getKeyCurrentStudend(AppDatabase.getAppDatabase(getContext()));

        mySubscribedsReference = FirebaseDatabase.getInstance().getReference("studentsCollection").child(senderUserId).
                child("Subscribed");
        mySubscribedsReference.keepSynced(true);

        students = FirebaseDatabase.getInstance().getReference("students");
        students.keepSynced(true);

        mySubscribedsList.setHasFixedSize(true);
        mySubscribedsList.setLayoutManager(new LinearLayoutManager(getContext()));
        // Inflate the layout for this fragment
        return myMainView;
    }

    public void setTextViewForEmptyList() {
        if (countSubscribeds != 0) {
            textViewDefaultText.setText("");
            textViewMySubscribeds.setText(getResources().getText(R.string.MySubscribeds) + " " + countSubscribeds);
        } else {
            progressBar.setVisibility(View.GONE);
            textViewDefaultText.setText(R.string.DefaultTextListMySubscribeds);
            textViewMySubscribeds.setText("");
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        mySubscribedsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countSubscribeds = dataSnapshot.getChildrenCount();
                setTextViewForEmptyList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (!network.isOnline()) {
                    //        progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        students.child(senderUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressBar.setVisibility(View.GONE);
                FindNewFriends findNewFriends = new FindNewFriends();
                SerieIDCard = findNewFriends.getStudentSeriesIDCard(AppDatabase.getAppDatabase(getContext()));
                FirebaseRecyclerAdapter<MySubscribers, MySubscribedsViewHolder> firebaseRecyclerAdapter
                        = new FirebaseRecyclerAdapter<MySubscribers, MySubscribedsViewHolder>
                        (
                                MySubscribers.class,
                                R.layout.my_subscribed_layout,
                                MySubscribedsViewHolder.class,
                                mySubscribedsReference

                        ) {
                    @Override
                    protected void populateViewHolder(final MySubscribedsViewHolder mySubscribedsViewHolder, final MySubscribers mySubscribers, final int i) {
                        setTextViewForEmptyList();
                        final String currentFriend = getRef(i).getKey();
                        students.child(currentFriend).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String name = dataSnapshot.child("name").getValue().toString();
                                String lastName = dataSnapshot.child("lastName").getValue().toString();
                                String grop = dataSnapshot.child("group").getValue().toString();
                                final String seriesIDcard = dataSnapshot.child("seriesIDcard").getValue().toString();
                                boolean online;
                                try {
                                    online = (boolean) dataSnapshot.child("online").getValue();
                                }catch (Exception e){
                                    online = false;
                                }
                                if (online){
                                    mySubscribedsViewHolder.setOnlineImage();
                                }
                                String linkFirebaseStorageMainPhoto;
                                try {
                                    linkFirebaseStorageMainPhoto = dataSnapshot.child("linkFirebaseStorageMainPhoto").getValue().toString();
                                }
                                catch (NullPointerException nullPointerException){
                                    linkFirebaseStorageMainPhoto ="";
                                }
                                mySubscribedsViewHolder.setStudentName(name, lastName);
                                mySubscribedsViewHolder.setStudentGroup(grop);
                                if (linkFirebaseStorageMainPhoto != "" && myContex != null) {
                                    mySubscribedsViewHolder.setStudentImage(myContex, linkFirebaseStorageMainPhoto);
                                }
                                mySubscribedsViewHolder.actionButton(currentFriend);
                                mySubscribedsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (!seriesIDcard.equals(SerieIDCard)) {
                                            String VisitedStudentKey = getRef(i).getKey();
                                            Intent profileIntent = new Intent("com.social_network.pnu_app.pages.ProfileStudent");
                                            profileIntent.putExtra("VisitedStudentKey", VisitedStudentKey);
                                            startActivity(profileIntent);
                                        } else {
                                            Intent myProfileIntent = new Intent("com.social_network.pnu_app.pages.MainStudentPage");
                                            startActivity(myProfileIntent);

                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                if (!network.isOnline()) {
                                    //        progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), " Please Connect to Internet",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                    }
                };
                mySubscribedsList.setAdapter(firebaseRecyclerAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (!network.isOnline()) {
                    //        progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }

            ;

        });

    }

    public void onAttach(Context context){
        super.onAttach(context);
        myContex = context;
    }

    String getKeyCurrentStudend(final AppDatabase db) {
        String keyStudent = db.studentDao().getKeyStudent();
        return keyStudent;
    }

    public void UnSubscribed(final String ReceiverStudentKey) {

        senderUserId = getKeyCurrentStudend(AppDatabase.getAppDatabase(getContext()));

        DatabaseReference SubscribedReferenceMy = FirebaseDatabase.getInstance().getReference("studentsCollection").child(senderUserId).child("Subscribed");
        SubscribedReferenceMy.keepSynced(true);
        SubscribedReferenceMy.child(ReceiverStudentKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DatabaseReference SubscribersReferenceAlien = FirebaseDatabase.getInstance().getReference("studentsCollection").child(ReceiverStudentKey).child("Subscribers");

                SubscribersReferenceAlien.child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (!network.isOnline() && myContex != null) {
                            //        progressBar.setVisibility(View.GONE);
                            Toast.makeText(myContex, " Please Connect to Internet",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (!network.isOnline() && myContex != null) {
                    //        progressBar.setVisibility(View.GONE);
                    Toast.makeText(myContex, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

class MySubscribedsViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public MySubscribedsViewHolder(View itemView){
        super(itemView);

        mView= itemView;
    }
    Button btnCancelMySubscribeds;

    public void actionButton(final String VisitedStudentKey) {

        btnCancelMySubscribeds =mView.findViewById(R.id.btnCancelMySubscribeds);

        btnCancelMySubscribeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCancelMySubscribeds.setEnabled(false);
                MySubscribedsFragment mySubscribedsFragment = new MySubscribedsFragment();
                mySubscribedsFragment.UnSubscribed(VisitedStudentKey);
                btnCancelMySubscribeds.setEnabled(true);
            }
        });


    }

    public void setOnlineImage(){

        ImageView imageOnline = mView.findViewById(R.id.img_online_subscribed);
        imageOnline.setVisibility(View.VISIBLE);
    }

    public void setStudentName(String studentName, String studentLastName){
        TextView nameAndLastName = mView.findViewById(R.id.mySubscribeds_username);
        nameAndLastName.setText(studentName + " " + studentLastName);
    }
    public void setStudentGroup(String studentGroup){
        TextView group = mView.findViewById(R.id.mySubscribeds_status);
        group.setText(studentGroup);
    }

    public void setStudentImage(final Context context, final String studentImage) {
        final CircleImageView image = mView.findViewById(R.id.mySubscribeds_profile_image);
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





