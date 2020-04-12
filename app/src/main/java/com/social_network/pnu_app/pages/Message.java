package com.social_network.pnu_app.pages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.MessageData;
import com.social_network.pnu_app.localdatabase.AppDatabase;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;


public class Message extends AppCompatActivity {

    private static int SIGN_IN_CODE = 1;
    private RelativeLayout relativeLayoutMessage;
    private FirebaseListAdapter<MessageData> adapter;
    private EmojiconEditText emojiconEditText;
    private ImageView emojiButton, submitButton;
    private EmojIconActions emojIconActions;

    String senderUserId;
    DatabaseReference studentsReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            Snackbar.make(relativeLayoutMessage, "Ви авторизовані", Snackbar.LENGTH_LONG).show();
        //    displayAllMessages();
        } else {
            Snackbar.make(relativeLayoutMessage, "Ви не авторизовані", Snackbar.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        relativeLayoutMessage = findViewById(R.id.rlMessenger);

        submitButton = findViewById(R.id.submit_button_menu);
        emojiButton = findViewById(R.id.emoji_button);
        emojiconEditText = findViewById(R.id.textField);
        emojIconActions = new EmojIconActions(getApplication(), relativeLayoutMessage, emojiconEditText, emojiButton);
        emojIconActions.ShowEmojIcon();


        ProfileStudent profileStudent = new ProfileStudent();
        senderUserId = profileStudent.getKeyCurrentStudend(AppDatabase.getAppDatabase(Message.this));
        studentsReference = FirebaseDatabase.getInstance().getReference("students").child(senderUserId);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("message").push().setValue(new MessageData(
                        FirebaseAuth.getInstance().getCurrentUser().toString(),
                        emojiconEditText.getText().toString()
                ));
                emojiconEditText.setText("");
            }
        });

        // Користувач не зареєстрований
    /*    if (FirebaseAuth.getInstance().getCurrentUser() == null)
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        else {*/
            Snackbar.make(relativeLayoutMessage, "Ви автризовані", Snackbar.LENGTH_LONG).show();
            displayAllMessages();
            //    mess_current_user.setText("current_user(displayName) = " + FirebaseAuth.getInstance().getCurrentUser().getUid() );
      //  }
    }

    private void displayAllMessages() {
        ListView listOfMessage = findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<MessageData>(this, MessageData.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference("message")) {
            //   @SuppressLint("WrongConstant")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            protected void populateView(View v, MessageData model, int position) {
                TextView mess_time;
                //   mess_time = v.findViewById(R.id.message_time);
                BubbleTextView mess_text, mess_text_current_user, mess_current_user;
                TextView mess_user;
                mess_user = v.findViewById(R.id.message_user);
                mess_text = v.findViewById(R.id.message_text);

                //    mess_text_current_user = v.findViewById(R.id.message_text_current_user);
                //              mess_current_user = v.findViewById(R.id.message_current_user);

              /*  Query getAllValueUserName;
                mess_text_current_user.setText(FirebaseDatabase.getInstance().getReference().orderByKey("userName"));
                System.out.println("mess_text_current_user " + mess_text_current_user);
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName();*/

             //   model.userName = "2";// FirebaseAuth.getInstance().getCurrentUser().toString();

       //         if (model.userName != null || model.userName !="") {
/*

                    mess_user.setBackgroundResource(R.drawable.rectangle_rounded_all);

                    //  mess_user.setBackgroundColor(Color.parseColor("#FFFAC733"));

                    RelativeLayout.LayoutParams paramsEmail = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                    paramsEmail.addRule(RelativeLayout.ALIGN_PARENT_END);
                    mess_user.setLayoutParams(paramsEmail);

                    RelativeLayout.LayoutParams paramsName = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                    paramsName.addRule(RelativeLayout.BELOW, R.id.message_user);
                    paramsName.addRule(RelativeLayout.ALIGN_PARENT_END);
                    mess_text.setLayoutParams(paramsName);

                    mess_user.setText("Ви " + "\n" + DateFormat.format("dd-MM-yyyy HH:mm:ss", model.getMessageTime()));
                    mess_text.setText(model.getTextMessage());

                    //       mess_user.setForegroundGravity(Gravity.RIGHT);
                    //    mess_current_user.setText(model.getUserName() + "\n" + DateFormat.format("dd-MM-yyyy HH:mm:ss", model.getMessageTime()));
                    //   mess_text_current_user.setText(model.getTextMessage());

                } else {*/

                    mess_user.setBackgroundResource(R.drawable.rectangle_rounded_all_rights);
                    mess_user.setText(model.getUserName() + "\n" + DateFormat.format("dd-MM-yyyy HH:mm:ss", model.getMessageTime()));
                    mess_text.setText(model.getTextMessage());

                    RelativeLayout.LayoutParams paramsEmail = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                    paramsEmail.setMarginStart(0);
                    mess_user.setLayoutParams(paramsEmail);

                    RelativeLayout.LayoutParams paramsName = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                    paramsName.addRule(RelativeLayout.BELOW, R.id.message_user);
                    paramsName.addRule(RelativeLayout.ALIGN_PARENT_START);
                    mess_text.setLayoutParams(paramsName);
             //   }
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
