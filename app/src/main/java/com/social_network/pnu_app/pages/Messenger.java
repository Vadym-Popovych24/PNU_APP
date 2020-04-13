package com.social_network.pnu_app.pages;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.MessageData;
import com.social_network.pnu_app.localdatabase.AppDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Messenger extends AppCompatActivity {



    ImageView imageMessenger;
    ImageView imgOnlineMessenger;
    ImageView imgSeenMessage;
    TextView tvMessengerUsername;
    TextView tvLastMessage;

    String senderUserId;
    DatabaseReference myMessageReference;
    DatabaseReference studentsReference;
    Query lastMessageQuery;

    HashMap<Object, Object> objectLastMessage = new HashMap();
    String lastMessage;
    String key;
    private FirebaseListAdapter<MessageData> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_messenger);
        bottomNavigationView.setSelectedItemId(R.id.action_message);

        menuChanges(bottomNavigationView);

        ProfileStudent profileStudent = new ProfileStudent();
        senderUserId = profileStudent.getKeyCurrentStudend(AppDatabase.getAppDatabase(Messenger.this));
        studentsReference = FirebaseDatabase.getInstance().getReference("students");
        myMessageReference = FirebaseDatabase.getInstance().getReference("students").child(senderUserId).child("Messages");



        displayAllMessengers();
    }


    private void displayAllMessengers() {

        ListView listOfMessage = findViewById(R.id.list_of_messengers);
        adapter = new FirebaseListAdapter<MessageData>(
                this,
                MessageData.class,
                R.layout.messenger_layout,
                myMessageReference) {
            //   @SuppressLint("WrongConstant")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            protected void populateView(final View viewLayout, MessageData model, int position) {

                imageMessenger = viewLayout.findViewById(R.id.image_messenger);
                imgOnlineMessenger = viewLayout.findViewById(R.id.img_online_messenger);
                imgSeenMessage = viewLayout.findViewById(R.id.img_seenMessage);
                tvMessengerUsername = viewLayout.findViewById(R.id.MessengerUsername);
                tvLastMessage = viewLayout.findViewById(R.id.tvLastMessage);

                final String currentMessengers = getRef(position).getKey();
                studentsReference.child(currentMessengers).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String name = dataSnapshot.child("name").getValue().toString();
                        final String lastName = dataSnapshot.child("lastName").getValue().toString();
                        lastMessageQuery = FirebaseDatabase.getInstance().getReference("students")
                                .child(senderUserId)
                                .child("Messages")
                                .child(currentMessengers).limitToLast(1);
                        lastMessageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    objectLastMessage = (HashMap<Object, Object>) snapshot.getValue();
                                    lastMessage = (String) objectLastMessage.get("message");
                                    key = (String) objectLastMessage.get("key");
                                    if (key.equals(senderUserId)) {
                                        tvLastMessage.setText("Ви: " + lastMessage);
                                    }
                                    else{
                                        tvLastMessage.setText(lastMessage);
                                    }
                                }



                                System.out.println("lastMessage = " + lastMessage);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        boolean online;
                        try {
                            online = (boolean) dataSnapshot.child("online").getValue();
                        }catch (Exception e){
                            online = false;
                        }
                        if (online){
                            imgOnlineMessenger.setVisibility(View.VISIBLE);
                        }
                        String linkFirebaseStorageMainPhoto;
                        try {
                            linkFirebaseStorageMainPhoto = dataSnapshot.child("linkFirebaseStorageMainPhoto").getValue().toString();
                        }
                        catch (NullPointerException nullPointerException){
                            linkFirebaseStorageMainPhoto ="";
                        }


                        tvMessengerUsername.setText(name + " " + lastName);


                        final String finalLinkFirebaseStorageMainPhoto = linkFirebaseStorageMainPhoto;
                        Picasso.with(getApplicationContext())
                                .load(linkFirebaseStorageMainPhoto)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                                .error(R.drawable.com_facebook_close)
                                .centerCrop()
                                .fit()
                                //.resize(1920,2560)
                                .into(imageMessenger, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        if (finalLinkFirebaseStorageMainPhoto != null) {
                                            if (!finalLinkFirebaseStorageMainPhoto.isEmpty()) {
                                                Picasso.with(getApplicationContext())
                                                        .load(finalLinkFirebaseStorageMainPhoto)
                                                        .placeholder(R.drawable.logo_pnu)
                                                        .error(R.drawable.com_facebook_close)
                                                        .centerCrop()
                                                        .fit()
                                                        //.resize(1920,2560)
                                                        .into(imageMessenger);
                                            }
                                        } else {
                                            Picasso.with(getApplicationContext())
                                                    .load(R.drawable.com_facebook_profile_picture_blank_square)
                                                    .placeholder(R.drawable.logo_pnu)
                                                    .error(R.drawable.com_facebook_close)
                                                    .centerCrop()
                                                    .fit()
                                                    //.resize(1920,2560)
                                                    .into(imageMessenger);
                                        }

                                    }
                                });

                        viewLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intentToMessage = new Intent(Messenger.this, Message.class);
                                intentToMessage.putExtra("VisitedStudentKey", currentMessengers);
                                startActivity(intentToMessage);

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }

        };
        listOfMessage.setAdapter(adapter);
    }


    public void menuChanges(BottomNavigationView bottomNavigationView){

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Intent intentMenu;
                        switch (item.getItemId()) {
                            case R.id.action_search:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.Search");
                                startActivity(intentMenu);

                                break;
                            case R.id.action_message:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.Messenger");
                                startActivity(intentMenu);


                                break;
                            case R.id.action_main_student_page:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.MainStudentPage");
                                startActivity(intentMenu);

                                break;
                            case R.id.action_schedule:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.Schedule");
                                startActivity(intentMenu);

                                break;
                            case R.id.action_settings:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.Settings");
                                startActivity(intentMenu);

                                break;
                        }
                        return false;
                    }
                });
    }

    private void lastSeen() {
        studentsReference.child(senderUserId).child("lastSeen").setValue(ServerValue.TIMESTAMP);
    }

    private void onlineStatus(final boolean online) {
        studentsReference.child(senderUserId).child("online").setValue(online);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // TODO delete comment  if (currentStudent != null){
        onlineStatus(false);
        lastSeen();
        //  }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO delete comment     if (currentStudent != null){
        onlineStatus(true);
        //    }
    }
}
