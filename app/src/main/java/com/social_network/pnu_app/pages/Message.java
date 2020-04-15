package com.social_network.pnu_app.pages;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.MessageData;
import com.social_network.pnu_app.functional.LastSeenTime;
import com.social_network.pnu_app.localdatabase.AppDatabase;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;


public class Message extends AppCompatActivity {

    TextView tvMessageName;
    TextView tvMessageLastSeenUser;
    CircleImageView message_profile_image;
    CircleImageView img_online_message;
    RelativeLayout relativeLayoutTop;
    Button btnBackFromMessage;

    private RelativeLayout relativeLayoutMessage;
    private FirebaseListAdapter<MessageData> adapter;
    private EmojiconEditText emojiconEditText;
    private ImageView emojiButton, submitButton;
    private EmojIconActions emojIconActions;

    String senderUserId;
    DatabaseReference studentsReference;
    DatabaseReference studentReceiver;
    DatabaseReference messageMyReference;
    DatabaseReference messageAlienReference;
    LastSeenTime objTimeLastMessage = new LastSeenTime();

    String name;
    String lastName;
    String linkFirebaseStorageMainPhoto;
    long lastSenn;
    boolean online;
    long time;
    String timeLastMessage;

    static String ReceiverStudentKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        relativeLayoutMessage = findViewById(R.id.rlMessenger);

        submitButton = findViewById(R.id.submit_button_menu);
        emojiButton = findViewById(R.id.emoji_button);
        emojiconEditText = findViewById(R.id.editTextMessage);
        emojIconActions = new EmojIconActions(getApplication(), relativeLayoutMessage, emojiconEditText, emojiButton);
        emojIconActions.ShowEmojIcon();

        tvMessageName = findViewById(R.id.tvMessageName);
        tvMessageLastSeenUser = findViewById(R.id.tvMessageLastSeenUser);
        message_profile_image = findViewById(R.id.message_profile_image);
        img_online_message = findViewById(R.id.img_online_message);
        btnBackFromMessage = findViewById(R.id.btnBackFromMessage);
        relativeLayoutTop = findViewById(R.id.RLMessageTop);
        btnBackFromMessage.setOnClickListener(btnlistener);


        ReceiverStudentKey = getIntent().getExtras().get("VisitedStudentKey").toString();

        ProfileStudent profileStudent = new ProfileStudent();
        senderUserId = profileStudent.getKeyCurrentStudend(AppDatabase.getAppDatabase(Message.this));
        studentsReference = FirebaseDatabase.getInstance().getReference("students").child(senderUserId);

        studentReceiver = FirebaseDatabase.getInstance().getReference("students").child(ReceiverStudentKey);

        messageMyReference = FirebaseDatabase.getInstance().getReference("students").child(senderUserId)
                .child("Messages").child(ReceiverStudentKey);

        messageAlienReference= FirebaseDatabase.getInstance().getReference("students").child(ReceiverStudentKey)
                .child("Messages").child(senderUserId);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                messageMyReference.push().setValue(new MessageData(
                        emojiconEditText.getText().toString(),
                        false,
                        "text",
                        senderUserId
                ));
                messageAlienReference.push().setValue(new MessageData(
                        emojiconEditText.getText().toString(),
                        false,
                        "text",
                        senderUserId
                ));

                emojiconEditText.setText("");
            }
        });
        relativeLayoutTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(Message.this, ProfileStudent.class);
                profileIntent.putExtra("VisitedStudentKey", ReceiverStudentKey);
                startActivity(profileIntent);
            }
        });
    }

    View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnBackFromMessage:
                    Intent intentlistMyFriends;
                    intentlistMyFriends = new Intent( "com.social_network.pnu_app.pages.Messenger");
                    startActivity(intentlistMyFriends);
                    break;
            }
        }
    };

    public void setTopDate(){
        studentReceiver.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    name = dataSnapshot.child("name").getValue().toString();
                } catch (Exception e) {
                    name = "";
                }
                try {
                    lastName = dataSnapshot.child("lastName").getValue().toString();
                } catch (Exception e) {
                    lastName = "";
                }
                try {
                    online = (boolean) dataSnapshot.child("online").getValue();
                } catch (Exception e) {
                    online = false;
                }
                try {
                    lastSenn = (long) dataSnapshot.child("lastSeen").getValue();
                } catch (Exception e) {
                    lastSenn = 0;
                }
                try {
                    linkFirebaseStorageMainPhoto = dataSnapshot.child("linkFirebaseStorageMainPhoto").getValue().toString();
                }
                catch (NullPointerException nullLinkPhoto){
                    linkFirebaseStorageMainPhoto = "";
                }

                tvMessageName.setText(lastName + " " + name);
                if (online) {
                    img_online_message.setVisibility(View.VISIBLE);
                    tvMessageLastSeenUser.setText(getString(R.string.Online));
                }
                else {
                    if (lastSenn != 0) {
                        LastSeenTime getTime = new LastSeenTime();
                        long lastSeenTime = Long.parseLong(String.valueOf(lastSenn));
                        String lastSeenDisplayTime = getTime.getTimeAgo(lastSeenTime);
                        tvMessageLastSeenUser.setText(lastSeenDisplayTime);
                    } else {
                        tvMessageLastSeenUser.setText(getString(R.string.NotOnline));
                    }
                }
                if (!linkFirebaseStorageMainPhoto.isEmpty()){
                    Picasso.with(Message.this)
                            .load(linkFirebaseStorageMainPhoto)
                            .placeholder(R.drawable.logo_pnu)
                            .error(R.drawable.com_facebook_close)
                            .centerCrop()
                            .fit()
                            // .resize(1920,2560)
                            .into(message_profile_image);

                }   else {
                    Picasso.with(Message.this)
                            .load(R.drawable.com_facebook_profile_picture_blank_square)
                            .placeholder(R.drawable.logo_pnu)
                            .error(R.drawable.com_facebook_close)
                            .centerCrop()
                            .fit()
                            // .resize(1920,2560)
                            .into(message_profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        setTopDate();
        ListView listOfMessage = findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<MessageData>(
                this, MessageData.class,
                R.layout.list_item,
                messageMyReference){
            //   @SuppressLint("WrongConstant")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            protected void populateView(View v, MessageData model, int position) {
                TextView message_text;
                message_text = v.findViewById(R.id.message_text);
                TextView message_time = v.findViewById(R.id.message_time);

                if (model.getKey().equals(senderUserId)) {

                    message_text.setBackgroundResource(R.drawable.rectangle_rounded_all);
                    //  mess_user.setText(model.getKey() + "\n" + DateFormat.format("dd-MM-yyyy HH:mm:ss", model.getTime()));

                    RelativeLayout.LayoutParams paramsName = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                    paramsName.addRule(RelativeLayout.ALIGN_PARENT_END);
                    message_text.setLayoutParams(paramsName);


                    message_text.setText(model.getMessage());
                    time = model.getTime();
                    timeLastMessage= objTimeLastMessage.getTimeMessenger(time);
                    message_time.setText(timeLastMessage);


                } else {

                    message_text.setBackgroundResource(R.drawable.rectangle_rounded_all_rights);
                  //  mess_user.setText(model.getKey() + "\n" + DateFormat.format("dd-MM-yyyy HH:mm:ss", model.getTime()));

                    RelativeLayout.LayoutParams paramsName = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                    paramsName.addRule(RelativeLayout.ALIGN_PARENT_START);
                    message_text.setLayoutParams(paramsName);

                    message_text.setText(model.getMessage());

                    time = model.getTime();
                    timeLastMessage= objTimeLastMessage.getTimeMessenger(time);
                    message_time.setText(timeLastMessage);
                }
            }

        };
        listOfMessage.setAdapter(adapter);
    }

    private void lastSeen() {
        studentsReference.child("lastSeen").setValue(ServerValue.TIMESTAMP);
    }

    private void onlineStatus(final boolean online) {
        studentsReference.child("online").setValue(online);
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
